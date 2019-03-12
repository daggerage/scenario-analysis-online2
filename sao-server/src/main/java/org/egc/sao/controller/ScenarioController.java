package org.egc.sao.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.domain.StructBMP;
import org.egc.sao.service.PlantBMPService;
import org.egc.sao.service.StructBMPService;
import org.ini4j.BasicMultiMap;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

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
            @RequestParam(value = "structBmp",required = false) String structBMP,
            @RequestParam(value = "plantBmp",required = false) String plantBMP,
            @RequestParam(required = false) String configUnit,
            @RequestParam(required = false) String configMethod,
            @RequestParam(required = false) String algorithm
    )throws IOException{
        System.out.println(structBMP);
        System.out.println(plantBMP);
        System.out.println(configUnit);
        System.out.println(configMethod);
        System.out.println(algorithm);

        String[] structBMPIds=structBMP.split(",");
        ArrayList<String> notFoundStructBMPIds=new ArrayList<>();
        ArrayList<Integer> structBMPSubscenarioes=new ArrayList<>();
        for(String structBMPId:structBMPIds){
            StructBMP bmp= sbs.findById(structBMPId);
            if (bmp!=null){
                Integer id= bmp.getSubscenario();
                structBMPSubscenarioes.add(id);
            }else {
                notFoundStructBMPIds.add(structBMPId);
            }
        }
        if(!notFoundStructBMPIds.isEmpty()){
            return new Result<>(ResInfo.NOT_FOUND,notFoundStructBMPIds);
        }

        File templateFile=new File("data/template/scenario_analysis.ini");
        Ini ini = new Ini(templateFile);
        Ini.Section  sectionBMP=ini.get("BMPs");
        JSONObject jo=JSON.parseObject(sectionBMP.get("BMPs_info"));
        JSONArray ja= jo
                .getJSONObject("17")
                .getJSONArray("SUBSCENARIO");
        ja.clear();
        ja.addAll(structBMPSubscenarioes);
        sectionBMP.put("BMPs_info",jo);
        if(writeIniToFile(ini,"data/user-config/saotest.ini")){
            return new Result<String>(ResInfo.SUCCESS,structBMP);

        }else{
            return new Result<Object>(ResInfo.INTERNAL_ERROR,null);
        }
    }

    private static boolean writeIniToFile(Ini ini,String outputPath) throws IOException{
        File output=new File(outputPath);
        if (!output.exists()&&!output.createNewFile()){
            return false;
        }
        StringWriter sw=new StringWriter();
        ini.store(sw);
        String outStr= sw.toString().replaceAll("\\\\","");
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
        bw.write(outStr);
        bw.flush();

        sw.close();
        bw.close();
        return true;
    }
}













