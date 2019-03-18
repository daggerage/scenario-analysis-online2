package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.Param;
import org.egc.sao.domain.ScenarioAnalysisResult;
import org.egc.sao.domain.User;

import java.util.List;
import java.util.UUID;

public interface ScenarioAnalysisResultDao {
    void insert(ScenarioAnalysisResult scenarioAnalysisResult);
    void update(ScenarioAnalysisResult scenarioAnalysisResult);
    List<ScenarioAnalysisResult> findAll(@Param(value = "scenarioAnalysisResult") ScenarioAnalysisResult scenarioAnalysisResult);
    List<ScenarioAnalysisResult> findAllUrl(UUID[] uuids);
}
