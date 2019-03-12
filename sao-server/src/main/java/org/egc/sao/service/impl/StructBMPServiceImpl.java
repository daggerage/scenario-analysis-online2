package org.egc.sao.service.impl;

import org.egc.sao.dao.mongodb.StructBMPDao;
import org.egc.sao.domain.StructBMP;
import org.egc.sao.service.StructBMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StructBMPServiceImpl implements StructBMPService {
    private final StructBMPDao dao;

    @Autowired
    public StructBMPServiceImpl(StructBMPDao dao){this.dao=dao;}

    @Override
    public List<StructBMP> findAll(){return dao.findAll();}

    @Override
    public List<StructBMP> findAllBySubscenario(int subscenario){return dao.findAllBySubscenario(subscenario);}

    @Override
    public StructBMP findById(String id){return dao.findById(id).orElse(null);}

    @Override
    public List<StructBMP> findAllByIds(String[] ids) {
        ArrayList<StructBMP> result=new ArrayList<>();
        for(String id:ids){
            result.add(dao.findById(id).orElse(null));
        }
        return result;
    }

    @Override
    public List<StructBMP> insert(List<StructBMP> asms) {
        return dao.insert(asms);
    }

    @Override
    public StructBMP insert(StructBMP asms) {
        return dao.insert(asms);
    }
}
