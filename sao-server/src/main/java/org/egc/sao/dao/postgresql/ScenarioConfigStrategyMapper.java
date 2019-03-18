package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.egc.sao.domain.OptimizeAlgorithm;
import org.egc.sao.domain.ScenarioConfigStrategy;

@Mapper
public interface ScenarioConfigStrategyMapper {

    @Select("select * from scenario_config_strategy where name = #{name}")
    ScenarioConfigStrategy findByName(@Param("name") String name);

    @Select("select * from scenario_config_strategy where id = #{id}")
    ScenarioConfigStrategy findById(@Param("id") int id);
}
