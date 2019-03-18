package org.egc.sao.dao.postgresql;

import org.apache.ibatis.annotations.*;
import org.egc.sao.domain.OptimizeAlgorithm;

@Mapper
public interface OptimizeAlgorithmMapper {

//    @Results({
//        @Result(property = "id",column = "id"),
//        @Result(property = "name",column = "name"),
//        @Result(property = "description",column = "description")
//    })

    @Select("select * from optimize_algorithm where name = #{name}")
    OptimizeAlgorithm findByName(@Param("name") String name);

    @Select("select * from optimize_algorithm where id = #{id}")
    OptimizeAlgorithm findById(@Param("id") int id);
}
