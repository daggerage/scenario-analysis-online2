package org.egc.sao;

import org.egc.sao.config.PathConfig;
import org.egc.sao.dao.postgresql.OptimizeAlgorithmMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MappersTest {

    @Autowired
    OptimizeAlgorithmMapper oam;

    @Test
    public void whateverIWantToTest(){
        System.out.println(PathConfig.SEIMS);
        System.out.println(PathConfig.DATA);
        System.out.println(PathConfig.MODEL_PATH);
        System.out.println(PathConfig.ANALYSIS_SCRIPT_PATH);
        System.out.println(PathConfig.PROJECT_PATH);
    }
}
