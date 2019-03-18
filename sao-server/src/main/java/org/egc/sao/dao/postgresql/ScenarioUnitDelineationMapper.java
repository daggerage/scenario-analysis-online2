package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.egc.sao.domain.OptimizeAlgorithm;
import org.egc.sao.domain.ScenarioUnitDelineation;

@Mapper
public interface ScenarioUnitDelineationMapper {

    @Select("select * from scenario_unit_delineation where name = #{name}")
    ScenarioUnitDelineation findByName(@Param("name") String name);

    @Select("select * from scenario_unit_delineation where id = #{id}")
    ScenarioUnitDelineation findById(@Param("id") int id);
}
