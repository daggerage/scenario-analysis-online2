package org.egc.sao.service;

import org.egc.sao.domain.ScenarioRecord;

import java.util.List;

public interface ScenarioRecordService {

    void insert(ScenarioRecord sr);
    List<ScenarioRecord> findAll(ScenarioRecord sr);
}
