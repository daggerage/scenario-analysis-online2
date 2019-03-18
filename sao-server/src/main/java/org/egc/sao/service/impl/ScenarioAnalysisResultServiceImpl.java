package org.egc.sao.service.impl;

import org.egc.sao.dao.postgresql.ScenarioAnalysisResultDao;
import org.egc.sao.domain.ScenarioAnalysisResult;
import org.egc.sao.service.ScenarioAnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScenarioAnalysisResultServiceImpl implements ScenarioAnalysisResultService {

    private final ScenarioAnalysisResultDao sard;

    @Autowired
    public ScenarioAnalysisResultServiceImpl(ScenarioAnalysisResultDao sard) {
        this.sard = sard;
    }

    @Override
    public void insert(ScenarioAnalysisResult scenarioAnalysisResult) {
        sard.insert(scenarioAnalysisResult);
    }

    @Override
    public void update(ScenarioAnalysisResult scenarioAnalysisResult) {
        sard.update(scenarioAnalysisResult);
    }

    @Override
    public List<ScenarioAnalysisResult> findAll(ScenarioAnalysisResult scenarioAnalysisResult) {
        return sard.findAll(scenarioAnalysisResult);
    }

    @Override
    public List<ScenarioAnalysisResult> findAllUrl(String[] resultIds) {
        UUID[] ids=new UUID[resultIds.length];
        for (int i = 0; i < resultIds.length; i++) {
            ids[i]=UUID.fromString(resultIds[i]);
        }
        return sard.findAllUrl(ids);
    }
}
