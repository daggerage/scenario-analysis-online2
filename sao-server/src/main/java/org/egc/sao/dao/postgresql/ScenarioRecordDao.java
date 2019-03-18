package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.Param;
import org.egc.sao.domain.ScenarioRecord;

import java.util.List;

public interface ScenarioRecordDao {
    void insert(ScenarioRecord sr);
    List<ScenarioRecord> findAll(@Param(value = "sr") ScenarioRecord sr);

}
