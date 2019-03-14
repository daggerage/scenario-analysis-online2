package org.egc.sao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.egc.sao.domain.StructBMP;
import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.service.PlantBMPService;
import org.egc.sao.service.StructBMPService;
import org.egc.sao.util.AuthUtil;
import org.egc.sao.util.DateUtil;
import org.ini4j.Ini;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/scenario")
@CrossOrigin(origins = "http://localhost:7099", maxAge = 3600)
public class ScenarioController {

    private final StructBMPService sbs;
    private final PlantBMPService pbs;

    @Autowired
    public ScenarioController(StructBMPService sbs,PlantBMPService pbs) {
        this.sbs = sbs;
        this.pbs = pbs;
    }

    @RequestMapping(value = "analysis",method = RequestMethod.POST)
    public Result saveStructBMP(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "structBmp",required = false) String structBMP,
            @RequestParam(value = "plantBmp",required = false) String plantBMP,
            @RequestParam(value = "configUnit", required = false) String configUnit,
            @RequestParam(value = "configMethod", required = false) String configMethod,
            @RequestParam(value = "algorithm", required = false) String algorithm
    )throws IOException {
        if (!AuthUtil.isJwtValide(token)) {
            return new Result<>(ResInfo.AUTH_FAIL, token);
        }

        System.out.println("token : "+token);
        System.out.println("structBMP : "+structBMP);
        System.out.println("plantBMP : "+plantBMP);
        System.out.println("configUnit : "+configUnit);
        System.out.println("configMethod : "+configMethod);
        System.out.println("algorithm : "+algorithm);
        String[] structBMPIds = structBMP.split(",");
        ArrayList<String> notFoundStructBMPIds = new ArrayList<>();
        ArrayList<Integer> structBMPSubscenarioes = new ArrayList<>();
        for (String structBMPId : structBMPIds) {
            StructBMP bmp = sbs.findById(structBMPId);
            if (bmp != null) {
                Integer id = bmp.getSubscenario();
                structBMPSubscenarioes.add(id);
            } else {
                notFoundStructBMPIds.add(structBMPId);
            }
        }
        if (!notFoundStructBMPIds.isEmpty()) {
            return new Result<>(ResInfo.NOT_FOUND, notFoundStructBMPIds);
        }

        File templateFile = new File("data/template/scenario_analysis_template.ini");
        Wini ini = new Wini(templateFile);
        Ini.Section sectionBMP = ini.get("BMPs");

        JSONObject jo;
        JSONArray ja;
        //choose BMP to apply
        jo = JSON.parseObject(sectionBMP.get("BMPs_info"));
        ja = jo
                .getJSONObject("17")
                .getJSONArray("SUBSCENARIO");
        ja.clear();
        ja.addAll(structBMPSubscenarioes);
        sectionBMP.put("BMPs_info", jo);

        //choose configuration unit
        sectionBMP.put("BMPs_cfg_units",UNIT_MAP.get(configUnit));

        //choose configuration method
        sectionBMP.put("BMPs_cfg_method",configMethod);

        //TODO: choose optimization algorithm
        if(algorithm.equals("NONE")){
            ini.remove("NSGA2");
        }
        if (writeIniToFile(ini, UUID.fromString(token))) {
            return new Result<String>(ResInfo.SUCCESS, structBMP);
        } else {
            return new Result<Object>(ResInfo.INTERNAL_ERROR, null);
        }
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
    private static boolean writeIniToFile(Wini ini, UUID id) throws IOException{
        String dir = "data/user-config/"+id;
        File file=new File(dir);
        if(!file.exists()&&!file.mkdirs()){
            return false;
        }

        String filePath=dir+"/"+ DateUtil.getPureDateNow() +"_analysis.ini";
        ini.store(new File(filePath));
        return true;
    }
}













