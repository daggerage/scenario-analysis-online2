package org.egc.sao;

import org.egc.sao.config.PathConfig;
import org.egc.sao.dao.postgresql.OptimizeAlgorithmMapper;
import org.egc.sao.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MappersTest {

    @Autowired
    PathConfig pathConfig;
    @Autowired
    OptimizeAlgorithmMapper oam;

    @Test
    public void whateverIWantToTest(){
        System.out.println(pathConfig.resultPath);
        System.out.println(pathConfig.analysisResultPath);
        System.out.println(pathConfig.PROJECT_PATH);
    }
}
