package org.egc.sao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.ini4j.Ini;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/scenario")
@CrossOrigin(origins = "http://localhost:7099", maxAge = 3600)
public class ScenarioController {

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
        this.us=us;
        this.sbs = sbs;
        this.pbs = pbs;
        this.sms = sms;
        this.srs = srs;
        this.sars = sars;
        this.cts=cts;
        this.pathConfig=pathConfig;
    }

    @RequestMapping(value = "analysis",method = RequestMethod.POST)
    @Transactional
    public Result startAnalysis(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "structBmp",required = false) String structBMP,
            @RequestParam(value = "plantBmp",required = false,defaultValue = "RICEPADDYCROPROTATION") String plantBMP,
            @RequestParam(value = "configUnit", required = false, defaultValue = "CONNFIELD") String configUnit,
            @RequestParam(value = "configMethod", required = false, defaultValue = "UPDOWN") String configMethod,
            @RequestParam(value = "algorithm", required = false, defaultValue = "NSGA2") String algorithm,
            @RequestParam(value = "generationNum", required = false, defaultValue = "2") int generationsNum,
            @RequestParam(value = "populationSize", required = false, defaultValue = "4") int populationSize,
            @RequestParam(value = "title", required = false) String title
    )throws IOException, InterruptedException {
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

        File templateFile = new File("data/template/scenario_analysis_template.ini");
        Wini ini = new Wini(templateFile);
        Ini.Section sectionBMP = ini.get("BMPs");

        JSONObject jo;
        JSONArray ja;

        //choose BMP to apply
        jo = JSON.parseObject(sectionBMP.get("BMPs_info"));
        ja = jo.getJSONObject("17").getJSONArray("SUBSCENARIO");
        ja.clear();
        ja.addAll(structBMPSubscenarioes);
        sectionBMP.put("BMPs_info", jo);

        //choose configuration unit
        sectionBMP.put("BMPs_cfg_units", UNIT_MAP.get(configUnit));

        //choose configuration method
        sectionBMP.put("BMPs_cfg_method", configMethod);

        //TODO: choose optimization algorithm
        if (algorithm.equals("NONE")) {
            ini.remove("NSGA2");
        } else {
            Ini.Section sectionAlgorithm = ini.get("NSGA2");
            sectionAlgorithm.put("GenerationsNum", generationsNum);
            sectionAlgorithm.put("PopulationSize",populationSize);
        }

        UUID resultId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.now();
        String pureTimestamp = DateUtil.parseLocalDateTimeToString(date, DateUtil.PATTERN_PURE);
        String storageUrl = "data\\scenario\\" + user.getId() + "@" + user.getName() + "\\" + pureTimestamp;
        String resultUrl = storageUrl + "\\result";

        ini.get("SEIMS_Model").put("OUT_DIR", PathConfig.PROJECT_PATH + PathConfig.SEP + resultUrl);

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

        if (writeIniToFile(ini, user.getId(), user.getName(), pureTimestamp)) {
            cts.AnalysisCmd(
                    storageUrl,
                    //String.format("SA_%s_%s_%s_Gen_%d_Pop_%d", algorithm, configUnit, configMethod, generationsNum, populationSize),
                    resultId
            );
            return new Result<>(ResInfo.SUCCESS, structBMP);
        } else {
            return new Result<>(ResInfo.INTERNAL_ERROR, null);
        }
    }

    @RequestMapping(value = "record",method = RequestMethod.GET)
    public Result listAllAnalysisRecord(
            @RequestParam(value = "token") String token
    ){
        if(!AuthUtil.isJwtValide(token)){
            return new Result<>(ResInfo.AUTH_FAIL,null);
        }
        User user=us.findUser(new User().setId(UUID.fromString(token)));

        List<ScenarioRecord> records = srs.findAll(new ScenarioRecord().setAccountId(user.getId()));
        return new Result<>(ResInfo.SUCCESS,records);
    }
    @RequestMapping(value = "put",method = RequestMethod.GET)
    public Result updateRecordTitle(
            @RequestParam(value = "token") String token,
            @RequestParam(value="recordId") String recordId,
            @RequestParam(value="title") String title
    ) {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, null);
        }
        ScenarioRecord sr=srs.findAll(new ScenarioRecord().setId(UUID.fromString(recordId))).get(0);
        sr.setTitle(title);
        List<ScenarioRecord> records = srs.findAll(sr);
        return new Result<>(ResInfo.SUCCESS, title);
    }

    @RequestMapping(value = "result",method = RequestMethod.GET)
    public Result listAnalysisRecordDetail(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "resultIds") String resultIds
    )throws IOException{
        if(!AuthUtil.isJwtValide(token)){
            return new Result<>(ResInfo.AUTH_FAIL,null);
        }
        String[] resultIdArr=resultIds.split(",");
        List<ScenarioAnalysisResult> results = sars.findAllUrl(resultIdArr);
        JSONObject jo=new JSONObject();
        for (ScenarioAnalysisResult r:results){
            String title=srs.findAll(new ScenarioRecord().setScenarioAnalysisResultId(r.getId())).get(0).getTitle();
            jo.put(title,getResultsFromLog(r.getUrl()+"\\runtime.log"));
        }
        return new Result<>(ResInfo.SUCCESS,jo);
    }

    private static JSONArray getResultsFromLog(String url)throws IOException{
        Scanner  s = new Scanner(new FileInputStream(url));
        JSONArray ja=new JSONArray();
        if(s.hasNext()){
            String line = s.nextLine();
            int i1=line.indexOf(":");
            int i2=line.indexOf(',');
            int i3=line.lastIndexOf(":");
            int i4=line.lastIndexOf(' ');

            int gen =Integer.valueOf(line.substring(i1+2,i2));
            int pop =Integer.valueOf(line.substring(i3+2,i4));
            System.out.println(gen+"  "+pop);
            s.next();
            for (int i = 0; i < gen-1; i++) {
                for (int j = 0; j < pop+2; j++) {
                    s.nextLine();
                }
            }
            s.nextLine();
            s.nextLine();
            for (int i = 0; i < pop; i++) {
                line=s.nextLine();
                String[] values=line.split("[\\t\\s]");
                String[] genes = line.split("[\\[\\]]")[1].split(", ");
                JSONObject jo=new JSONObject();
                jo.put("economy",values[2]);
                jo.put("environment",values[3]);
                jo.put("gene",genes);
                ja.add(jo);
            }
            System.out.println(ja.toArray());
        }
        return ja;
    }

    private static HashMap<String,String> UNIT_MAP = new HashMap<String,String>(){
        {
            String CONFIG_UNIT_HRU = "{\"HRU\": {\"DISTRIBUTION\": \"RASTER|SPATIAL_NONUNIQUE_HRUS\", \"UNITJSON\": \"hru_units.json\"}}";
            String CONFIG_UNIT_EXPLICITHRU = "{\"EXPLICITHRU\": {\"DISTRIBUTION\": \"RASTER|SPATIAL_UNIQUE_HRUS\", \"UNITJSON\": \"explicit_hru_units.json\"}}";
            String CONFIG_UNIT_CONNFIELD = "{\"CONNFIELD\": {\"DISTRIBUTION\": \"RASTER|FIELDS_15\", \"UNITJSON\": \"connected_field_units_updown_15.json\"}}";
            String CONFIG_UNIT_SLPPOS = "{\"SLPPOS\": {\"DISTRIBUTION\": \"RASTER|SLPPOS_UNITS\", \"UNITJSON\": \"slppos_3cls_units_updown.json\", \"SLPPOS_TAG_NAME\": {\"1\": \"summit\", \"4\": \"backslope\", \"16\": \"valley\"}}}";
            put("HRU",CONFIG_UNIT_HRU);
            put("EXPLICITHRU",CONFIG_UNIT_EXPLICITHRU);
            put("CONNFIELD",CONFIG_UNIT_CONNFIELD);
            put("SLPPOS",CONFIG_UNIT_SLPPOS);
        }
    };
    private static boolean writeIniToFile(Wini ini, UUID id,String userName,String pureTimestamp) throws IOException{
        String targetDir=String.format("data\\scenario\\%s@%s/%s",id.toString(),userName,pureTimestamp);
        File file=new File(targetDir);
        if(!file.exists()&&!file.mkdirs()){
            return false;
        }

        String filePath=targetDir+"\\user_sa.ini";
        ini.store(new File(filePath));
        return true;
    }
}













