package org.egc.sao.controller;

import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.service.PlantBMPService;
import org.egc.sao.service.StructBMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
            @RequestParam String structBMP,
            @RequestParam String plantBmp,
            @RequestParam String configUnit,
            @RequestParam String configMethod,
            @RequestParam String algorithm
    ){
        System.out.println(structBMP);
        System.out.println(plantBmp);
        System.out.println(configUnit);
        System.out.println(configMethod);
        System.out.println(algorithm);
        return new Result<Object>(ResInfo.SUCCESS,structBMP);
    }
}
