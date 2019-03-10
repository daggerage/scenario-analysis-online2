package org.egc.sao.service.impl;

import org.egc.sao.dao.mongodb.PlantBMPDao;
import org.egc.sao.domain.PlantBMP;
import org.egc.sao.service.PlantBMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantBMPServiceImpl implements PlantBMPService {

    @Autowired
    PlantBMPDao dao;

    @Override
    public List<PlantBMP> findAll() {
        return dao.findAll();
    }
}
