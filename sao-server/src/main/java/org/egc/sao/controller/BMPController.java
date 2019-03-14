package org.egc.sao.controller;

import org.egc.sao.domain.StructBMP;
import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.domain.PlantBMP;
import org.egc.sao.service.PlantBMPService;
import org.egc.sao.service.StructBMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/bmp")
@CrossOrigin(origins = "http://localhost:7099", maxAge = 3600)
public class BMPController {
    private final StructBMPService sbs;
    private final PlantBMPService pbs;

    @Autowired
    public BMPController(StructBMPService sbs, PlantBMPService pbs) {
        this.sbs = sbs;
        this.pbs = pbs;
    }

    @RequestMapping(value = "struct",method = RequestMethod.GET)
    public Result listAllStructBMP(){
        List<StructBMP> bmps= sbs.findAll();
        if (bmps.size()==0){
            return new Result<>(ResInfo.NOT_FOUND,null);
        }
        for (StructBMP bmp:bmps){
            String paramString=bmp.getParameters();
            bmp.setParamDetail(splitParams(paramString));//分割StructBMP的参数
        }
        return new Result<>(ResInfo.SUCCESS,bmps);
    }

    @RequestMapping(value = "struct",method = RequestMethod.PUT)
    public Result saveStructBMP(
            @RequestParam String name,
            @RequestParam String reference,
            @RequestParam int effectiveness
            //omitted...it's just for test and not in use currently
    ){
        StructBMP asm=new StructBMP();
        asm.setName(name)
                .setReference(reference)
                .setEffectiveness(effectiveness);

        StructBMP result= sbs.insert(asm);
        return new Result<>(ResInfo.SUCCESS,result);
    }

    @RequestMapping(value = "plant",method = RequestMethod.GET)
    public Result listAllPlantBMP(){
        List<PlantBMP> bmps= pbs.findAll();
        if (bmps.size()==0){
            return new Result<>(ResInfo.NOT_FOUND,null);
        }
        bmps.forEach(PlantBMP::constructParamDetail);
        return new Result<>(ResInfo.SUCCESS,bmps);
    }
    /*
    *
    * Input Example:
    *   "OM:ORGANIC MATTER:RC:2.05-DENSITY:BULK DENSITY:RC:0.96-POROSITY:TOTAL POROSITY:RC:1.03"
    * OutPut Example:
    *   [["OM","ORGANIC MATTER","RC","2.05"],[...],[...]]
     */
    private static ArrayList<ArrayList<String>> splitParams(String paramString){
        if(paramString==null)
            return null;
        String[] params=paramString.split("-");
        ArrayList<ArrayList<String>> result=new ArrayList<>();
        for(String param:params){
            String[] fields=param.split(":");
            ArrayList<String> fieldList = new ArrayList<>(Arrays.asList(fields));
            result.add(fieldList);
        }
        return result;
    }
}
