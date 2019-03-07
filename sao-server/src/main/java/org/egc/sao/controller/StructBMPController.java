package org.egc.sao.controller;

import org.egc.sao.base.ResInfo;
import org.egc.sao.base.Result;
import org.egc.sao.service.BMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("structbmp")
public class StructBMPController {
    private final BMPService service;

    @Autowired
    public StructBMPController(BMPService service){this.service=service;}

    @RequestMapping(method = RequestMethod.GET)
    public Result findAllStructBMP(){
        System.out.println(1/0);
        return new Result<>(ResInfo.SUCCESS,service.findAll());
    }
}
