package org.egc.sao.config.async;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.egc.sao.config.PathConfig;
import org.egc.sao.dao.postgresql.ScenarioAnalysisResultDao;
import org.egc.sao.domain.ScenarioAnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class CmdTaskService {
    private final ScenarioAnalysisResultDao sard;
    private final PathConfig pathConfig;

    @Autowired
    public CmdTaskService(ScenarioAnalysisResultDao sard,PathConfig pathConfig) {
        this.sard = sard;
        this.pathConfig=pathConfig;
    }

    /**
     * 异步执行情景分析脚本，并将结果转储在指定路径
     * 此命令不支持并发执行，以互相免覆盖本地存储结果
     * TODO: 后期修改seims框架使其结果能保存在指定路径
     * @param storagePath 最终存储的路径，如 data/scenario/user-id@UserName/date_time
     * @param scenarioAnalysisResultId 情景分析记录的id
     */
    @Async
    public void AnalysisCmd(String storagePath, UUID scenarioAnalysisResultId) throws IOException,InterruptedException {
        String line = "python "+ pathConfig.analysisResultPath +" -ini "+PathConfig.PROJECT_PATH+PathConfig.SEP+storagePath+PathConfig.SEP+"user_sa.ini";
        System.out.println(line);

        String storageResultDirPath=PathConfig.PROJECT_PATH+PathConfig.SEP +storagePath+"\\result";
        System.out.println(storageResultDirPath);
        File storageResultDir = new File(storageResultDirPath);
        storageResultDir.mkdir();
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
