package org.egc.sao.service;

import org.egc.sao.domain.ScenarioAnalysisResult;

import java.util.List;

public interface ScenarioAnalysisResultService {

    void insert(ScenarioAnalysisResult scenarioAnalysisResult);
    void update(ScenarioAnalysisResult scenarioAnalysisResult);
    List<ScenarioAnalysisResult> findAll(ScenarioAnalysisResult scenarioAnalysisResult);
    List<ScenarioAnalysisResult> findAllUrl(String[] resultIds);

}
