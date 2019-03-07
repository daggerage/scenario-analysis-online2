package org.egc.sao.service.impl;

import org.egc.sao.dao.mongodb.ArealStructManagementDao;
import org.egc.sao.domain.ArealStructManagement;
import org.egc.sao.service.BMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BMPServiceImpl implements BMPService {
    private final ArealStructManagementDao dao;

    @Autowired
    public BMPServiceImpl(ArealStructManagementDao dao){this.dao=dao;}

    @Override
    public List<ArealStructManagement> findAll(){return dao.findAll();}

    @Override
    public List<ArealStructManagement> findAllBySubscenario(int subscenario){return dao.findAllBySubscenario(subscenario);}
}
