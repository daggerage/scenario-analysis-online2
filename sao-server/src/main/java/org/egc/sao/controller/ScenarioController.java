package org.egc.sao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.egc.sao.config.PathConfig;
import org.egc.sao.config.async.CmdTaskService;
import org.egc.sao.domain.ScenarioAnalysisResult;
import org.egc.sao.domain.ScenarioRecord;
import org.egc.sao.domain.StructBMP;
import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.domain.User;
import org.egc.sao.service.*;
import org.egc.sao.util.AuthUtil;
import org.egc.sao.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("api/v1/scenario")
//@CrossOrigin(origins = "http://localhost:7099", maxAge = 3600)
public class ScenarioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioController.class);
    private final UserService us;
    private final StructBMPService sbs;
    private final PlantBMPService pbs;
    private final ScenarioMapperService sms;
    private final ScenarioRecordService srs;
    private final ScenarioAnalysisResultService sars;
    private final CmdTaskService cts;
    private final PathConfig pathConfig;

    @Autowired
    public ScenarioController(
            UserService us,
            StructBMPService sbs,
            PlantBMPService pbs,
            ScenarioMapperService sms,
            ScenarioRecordService srs,
            ScenarioAnalysisResultService sars,
            CmdTaskService cts,
            PathConfig pathConfig
    ) {
        this.us = us;
        this.sbs = sbs;
        this.pbs = pbs;
        this.sms = sms;
        this.srs = srs;
        this.sars = sars;
        this.cts = cts;
        this.pathConfig = pathConfig;
    }

    @RequestMapping(value = "analysis", method = RequestMethod.POST)
    @Transactional
    public Result startAnalysis(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "structBmps", required = false) String structBMP,
            @RequestParam(value = "plantBmps", required = false, defaultValue = "RICEPADDYCROPROTATION") String plantBMP,
            @RequestParam(value = "configUnit", required = false, defaultValue = "CONNFIELD") String configUnit,
            @RequestParam(value = "configMethod", required = false, defaultValue = "UPDOWN") String configMethod,
            @RequestParam(value = "algorithm", required = false, defaultValue = "NSGA2") String algorithm,
            @RequestParam(value = "maxEconomy", required = false, defaultValue = "300") double maxEconomy,
            @RequestParam(value = "minEnvironment", required = false, defaultValue = "0") double minEnvironment,
            @RequestParam(value = "generationNum", required = false, defaultValue = "2") int generationsNum,
            @RequestParam(value = "populationSize", required = false, defaultValue = "4") int populationSize,
            @RequestParam(value = "title", required = false) String title
    ) throws IOException, InterruptedException {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, token);
        }
        title = title == null ? String.format("%s_%s_%s_Gen_%d_Pop_%d", algorithm, configUnit, configMethod, generationsNum, populationSize) : title;
        User user = us.findUser(new User().setId(UUID.fromString(token)));
        ArrayList<Integer> structBMPSubscenarioes = new ArrayList<>();

        if (structBMP == null) {
            sbs.findAll().forEach((sb) -> structBMPSubscenarioes.add(sb.getSubscenario()));
        } else {
            String[] structBMPIds = structBMP.split(",");
            ArrayList<String> notFoundStructBMPIds = new ArrayList<>();

            for (String structBMPId : structBMPIds) {
                StructBMP bmp = sbs.findById(structBMPId);
                if (bmp != null) {
                    structBMPSubscenarioes.add(bmp.getSubscenario());
                } else {
                    notFoundStructBMPIds.add(structBMPId);
                }
            }
            if (!notFoundStructBMPIds.isEmpty()) {
                return new Result<>(ResInfo.NOT_FOUND, notFoundStructBMPIds);
            }
        }

        Path templatePath = Paths.get(PathConfig.DATA,"template","scenario_analysis_template.ini");
        File templateFile = new File(templatePath.toString());
        INIConfiguration ini = new INIConfiguration();
        try {
            ini.read(new InputStreamReader(new FileInputStream(templateFile)));
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            return new Result<>(ResInfo.INTERNAL_ERROR, null);
        }
        JSONObject jo;
        JSONArray ja;

//        Ini.Section sectionCommon = ini.get("Scenario_Common");
//        sectionCommon.put("worst_economy",String.format("%.2f",maxEconomy));
//        sectionCommon.put("worst_environment",String.format("%.2f",minEnvironment));

        SubnodeConfiguration sectionBMP = ini.getSection("BMPs");

        //choose BMP to apply
        jo = JSON.parseObject(sectionBMP.getString("BMPs_info"));
        ja = jo.getJSONObject("17").getJSONArray("SUBSCENARIO");
        ja.clear();
        ja.addAll(structBMPSubscenarioes);
        sectionBMP.setProperty("BMPs_info", jo);

        //choose configuration unit
        sectionBMP.setProperty("BMPs_cfg_units", UNIT_MAP.get(configUnit));

        //choose configuration method
        sectionBMP.setProperty("BMPs_cfg_method", configMethod);

        //TODO: choose optimization algorithm
        if (algorithm.equals("NONE")) {
            ini.clearProperty("NSGA2");
        } else {
            SubnodeConfiguration sectionAlgorithm = ini.getSection("NSGA2");
            sectionAlgorithm.setProperty("GenerationsNum", generationsNum);
            sectionAlgorithm.setProperty("PopulationSize", populationSize);
            sectionAlgorithm.setProperty("EconomyThreshold", maxEconomy);
            sectionAlgorithm.setProperty("EnvironmentThreshold", minEnvironment);
        }

        UUID resultId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        String pureTimestamp = DateUtil.parseLocalDateTimeToString(date, DateUtil.PATTERN_PURE);
        Path storageUrl = Paths.get(PathConfig.DATA,"scenario",user.getId().toString()+"@"+user.getName(),pureTimestamp);
        String resultUrl = storageUrl + PathConfig.SEP + "result";

        //add MODEL_DIR, BIN_DIR and SA_OUT_DIR
        SubnodeConfiguration sectionSEIMS = ini.getSection("SEIMS_Model");
        sectionSEIMS.addProperty("MODEL_DIR", PathConfig.MODEL_PATH);
        sectionSEIMS.addProperty("BIN_DIR", PathConfig.SEIMS + PathConfig.SEP + "bin");
        sectionSEIMS.addProperty("SA_OUT_DIR", resultUrl);


        sars.insert(
                new ScenarioAnalysisResult()
                        .setId(resultId)
                        .setUrl(resultUrl)
        );
        srs.insert(
                new ScenarioRecord()
                        .setId(UUID.randomUUID())
                        .setAccountId(UUID.fromString(token))
                        .setCreatedOn(date)
                        .setScenarioAnalysisResultId(resultId)
                        .setScenarioConfigStrategyId(sms.findScenarioConfigStrategy(configMethod).getId())
                        .setScenarioUnitDelineationId(sms.findScenarioUnitDelineation(configUnit).getId())
                        .setOptimizeAlgorithmId(sms.findOptimizeAlgorithm(algorithm).getId())
                        .setTitle(title)
        );

        boolean isWriteSuccess = false;
        try {
            isWriteSuccess = writeIniToFile(ini, user.getId(), user.getName(), pureTimestamp);
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            return new Result<>(ResInfo.INTERNAL_ERROR, "write SEIMS ini file failed!");
        }
        if (isWriteSuccess) {
            cts.AnalysisCmd(
                    storageUrl.toString(),
                    resultId
            );
            return new Result<>(ResInfo.SUCCESS, structBMP);
        } else {
            return new Result<>(ResInfo.INTERNAL_ERROR, "executing async SEIMS task failed");
        }
    }

    @RequestMapping(value = "record", method = RequestMethod.GET)
    public Result listAllAnalysisRecord(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "resultId", required = false) String resultId
    ) {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, null);
        }
        User user = us.findUser(new User().setId(UUID.fromString(token)));


        List<ScenarioRecord> records = null;
        if(resultId==null){
            records=srs.findAll(new ScenarioRecord().setAccountId(user.getId()));
        }else{
            records=srs.findAll(
                    new ScenarioRecord().setAccountId(user.getId()).setScenarioAnalysisResultId(UUID.fromString(resultId))
            );
        }
        return new Result<>(ResInfo.SUCCESS, records);
    }

    //TODO: 修改记录标题
    @RequestMapping(value = "put", method = RequestMethod.GET)
    public Result updateRecordTitle(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "recordId") String recordId,
            @RequestParam(value = "title") String title
    ) {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, null);
        }
        ScenarioRecord sr = srs.findAll(new ScenarioRecord().setId(UUID.fromString(recordId))).get(0);
        sr.setTitle(title);
        List<ScenarioRecord> records = srs.findAll(sr);
        return new Result<>(ResInfo.SUCCESS, title);
    }

    @RequestMapping(value = "result", method = RequestMethod.GET)
    public Result listAnalysisRecordDetail(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "resultIds") String resultIds
    ) throws IOException {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, null);
        }
        String[] resultIdArr = resultIds.split(",");
        List<ScenarioAnalysisResult> results = sars.findAllUrl(resultIdArr);
        JSONObject jo = new JSONObject();
        for (int i = 0; i < results.size(); i++) {
            ScenarioAnalysisResult r = results.get(i);
            String title = srs.findAll(new ScenarioRecord().setScenarioAnalysisResultId(r.getId())).get(0).getTitle();
            JSONObject jojo = new JSONObject();
            jojo.put("id", r.getId());
            jojo.put("title", title);
            jojo.put("pops", getResultsFromLog(r.getUrl()));
            jo.put(String.valueOf(i), jojo);
        }
        return new Result<>(ResInfo.SUCCESS, jo);
    }

    @RequestMapping(value = "file", method = RequestMethod.GET)
//    public ResponseEntity<Resource> downloadScenarioFile(
    public Result downloadScenarioFile(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "resultId") String resultId,
            @RequestParam(value = "scenarioId") String scenarioId
    ) throws IOException {
        if (!AuthUtil.isJwtValide(token)) {
//            return ResponseEntity.badRequest()
//                    .header("no auth","no auth!")
//                    .body(null);
            return new Result<>(ResInfo.AUTH_FAIL, null);
        }
        ScenarioAnalysisResult result = sars.findAllUrl(new String[]{resultId}).get(0);
        String url = result.getUrl();
        return new Result<>(ResInfo.SUCCESS, url);

//        File zip=null;
//        InputStreamResource resource =null;
//        String zipFilePath=String.format("%s\\Scenarios\\Scenario_%s.zip",url,scenarioId);
//        File zipFile=new File(zipFilePath);
//        if(zipFile.exists()){
//            zipFile.delete();
//            return ResponseEntity.badRequest()
//                    .header("exists","exists!!")
//                    .body(null);
//        }else if(!zipFile.createNewFile()){
//            return ResponseEntity.badRequest()
//                    .header("cr","cr!!")
//                    .body(null);
//        }
//
//        File txtFile=new File(String.format("%s\\Scenarios\\Scenario_%s.txt",url,scenarioId));
//        File tifFile=new File(String.format("%s\\Scenarios\\Scenario_%s.tif",url,scenarioId));
//        try(
//                FileInputStream txtFis=new FileInputStream(txtFile);
//                FileInputStream tifFis=new FileInputStream(tifFile);
//                ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(zipFile));
//                ){
//            byte[] buf=new byte[4096];
//            zos.putNextEntry(new ZipEntry(txtFile.getName()));
//            int len=0;
//            while ((len=txtFis.read(buf))!=-1){
//                zos.write(buf,0,len);
//            }
//            zos.closeEntry();
//            txtFis.close();
//
//            zos.putNextEntry(new ZipEntry(tifFile.getName()));
//            len=0;
//            while ((len=tifFis.read(buf))!=-1){
//                zos.write(buf,0,len);
//            }
//            zos.closeEntry();
//            zos.finish();
//            zos.close();
//            zip=new File(zipFilePath);
//            resource = new InputStreamResource(new FileInputStream(zip));
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ojbk.zip");
//            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//            headers.add("Pragma", "no-cache");
//            headers.add("Expires", "0");
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(zip.length())
//                    .contentType(MediaType.parseMediaType("application/octet-stream"))
//                    .body(resource);
//
//        }finally {
//            //TODO: not working?
//            zipFile.delete();
//        }

    }

    private static JSONObject getResultsFromLog(String url) throws IOException {
        JSONObject jo = new JSONObject();
        try (Scanner s = new Scanner(new FileInputStream(url + PathConfig.SEP + "runtime.log"))) {
            if (s.hasNext()) {
                String line = s.nextLine();
                int i1 = line.indexOf(":");
                int i2 = line.indexOf(',');
                int i3 = line.lastIndexOf(":");
                int i4 = line.lastIndexOf(' ');

                int gen = Integer.valueOf(line.substring(i1 + 2, i2));
                int pop = Integer.valueOf(line.substring(i3 + 2, i4));
                LOGGER.debug(gen + "  " + pop);
                s.next();
                for (int i = 0; i < gen - 1; i++) {
                    for (int j = 0; j < pop + 2; j++) {
                        s.nextLine();
                    }
                }
                s.nextLine();
                s.nextLine();
                ArrayList<JSONObject> jojos = new ArrayList<>();
                for (int i = 0; i < pop; i++) {
                    line = s.nextLine();
                    String[] values = line.split("[\\t\\s]");

                    JSONObject jojo = new JSONObject();
                    jojo.put("scenario", values[1]);
                    jojo.put("economy", values[2]);
                    jojo.put("environment", values[3]);
                    jojos.add(jojo);
                    //先把上面的排序，然后再统一加下面的序号
                }
                jojos.sort((a, b) -> {
                    double av = Double.valueOf((String) a.get("economy"));
                    double bv = Double.valueOf((String) b.get("economy"));
                    return (int) (av - bv);
                });
                for (int i = 0; i < jojos.size(); i++) {
                    jo.put(String.valueOf(i), jojos.get(i));
                    LOGGER.debug(jojos.get(i).get("economy").toString());
                }

            }
            //TODO: remove these ugly codes

            for (String k : jo.keySet()) {
                JSONObject jojo = (JSONObject) jo.get(k);
                String scenario = jojo.getString("scenario");
                try (Scanner s1 = new Scanner(new FileInputStream(
                        String.format("%s%sScenarios%sScenario_%s.txt", url, PathConfig.SEP, PathConfig.SEP, scenario)))
                ) {
                    s1.nextLine();
                    s1.nextLine();
                    s1.nextLine();
                    s1.nextLine();
                    s1.nextLine();
                    JSONObject pairs = new JSONObject();
                    for (int i = 1; i < 5; i++) {
                        String line = s1.nextLine().split("[\\t\\s]")[4];
                        for (String key : line.split("-")) {
                            pairs.put(key, i);
                        }
                    }
                    jojo.put("pairs", pairs);
                }
            }
        }
        return jo;
    }

    private static HashMap<String, String> UNIT_MAP = new HashMap<String, String>() {
        {
            String CONFIG_UNIT_HRU = "{\"HRU\": {\"DISTRIBUTION\": \"RASTER|SPATIAL_NONUNIQUE_HRUS\", \"UNITJSON\": \"hru_units.json\"}}";
            String CONFIG_UNIT_EXPLICITHRU = "{\"EXPLICITHRU\": {\"DISTRIBUTION\": \"RASTER|SPATIAL_UNIQUE_HRUS\", \"UNITJSON\": \"explicit_hru_units.json\"}}";
            String CONFIG_UNIT_CONNFIELD = "{\"CONNFIELD\": {\"DISTRIBUTION\": \"RASTER|FIELDS_15\", \"UNITJSON\": \"connected_field_units_updown_15.json\"}}";
            String CONFIG_UNIT_SLPPOS = "{\"SLPPOS\": {\"DISTRIBUTION\": \"RASTER|SLPPOS_UNITS\", \"UNITJSON\": \"slppos_3cls_units_updown.json\", \"SLPPOS_TAG_NAME\": {\"1\": \"summit\", \"4\": \"backslope\", \"16\": \"valley\"}}}";
            put("HRU", CONFIG_UNIT_HRU);
            put("EXPLICITHRU", CONFIG_UNIT_EXPLICITHRU);
            put("CONNFIELD", CONFIG_UNIT_CONNFIELD);
            put("SLPPOS", CONFIG_UNIT_SLPPOS);
        }
    };

    private static boolean writeIniToFile(INIConfiguration ini, UUID id, String userName, String pureTimestamp) throws IOException, ConfigurationException {
        String targetDir = Paths.get(
                PathConfig.DATA,"scenario",id.toString()+"@"+userName,pureTimestamp).toString();
        File file = new File(targetDir);
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }

        String filePath = targetDir + PathConfig.SEP + "user_sa.ini";
        ini.write(new FileWriter(new File(filePath)));
        return true;
    }
}













