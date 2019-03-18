package org.egc.sao.service.impl;

import org.egc.sao.dao.postgresql.ScenarioRecordDao;
import org.egc.sao.domain.ScenarioRecord;
import org.egc.sao.service.ScenarioRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenarioRecordServiceImpl implements ScenarioRecordService {

    private final ScenarioRecordDao srd;

    @Autowired
    public ScenarioRecordServiceImpl(ScenarioRecordDao srd) {
        this.srd = srd;
    }

    public void insert(ScenarioRecord sr){
        srd.insert(sr);
    }
    public List<ScenarioRecord> findAll(ScenarioRecord sr){
        return srd.findAll(sr);
    }
}
