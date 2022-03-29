package org.egc.sao.config.async;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.egc.sao.config.PathConfig;
import org.egc.sao.controller.ScenarioController;
import org.egc.sao.dao.postgresql.ScenarioAnalysisResultDao;
import org.egc.sao.domain.ScenarioAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class CmdTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioController.class);
    private final ScenarioAnalysisResultDao sard;

    @Autowired
    public CmdTaskService(ScenarioAnalysisResultDao sard) {
        this.sard = sard;
    }

    /**
     * 异步执行情景分析脚本，并将结果转储在指定路径
     * @param storagePath 最终存储的路径，如 data/scenario/user-id@UserName/date_time
     * @param scenarioAnalysisResultId 情景分析记录的id
     */
    @Async
    public void AnalysisCmd(String storagePath, UUID scenarioAnalysisResultId) throws IOException {
        String line = "python "+ PathConfig.ANALYSIS_SCRIPT_PATH +" -ini "+PathConfig.PROJECT_PATH+PathConfig.SEP+storagePath+PathConfig.SEP+"user_sa.ini";
        LOGGER.info(line);

        String storageResultDirPath=PathConfig.PROJECT_PATH+PathConfig.SEP +storagePath+PathConfig.SEP+"result";
        LOGGER.info("Result stored to "+storageResultDirPath);
        File storageResultDir = new File(storageResultDirPath);
        storageResultDir.mkdirs();
        CommandLine cmdLine = CommandLine.parse(line);

        DefaultExecutor executor = new DefaultExecutor();
        executor.execute(cmdLine);

        sard.update(
                new ScenarioAnalysisResult()
                        .setId(scenarioAnalysisResultId)
                        .setResult(1)
        );
    }

}
