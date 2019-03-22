package org.egc.sao.service.impl;

import org.egc.sao.dao.mongodb.PlantBMPDao;
import org.egc.sao.domain.PlantBMP;
import org.egc.sao.service.PlantBMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantBMPServiceImpl implements PlantBMPService {

    private final PlantBMPDao dao;

    @Autowired
    public PlantBMPServiceImpl(PlantBMPDao dao) {
        this.dao = dao;
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public List<PlantBMP> findAll() {
        return dao.findAll();
    }
}
