//package org.egc.sao.config;
//
//import org.apache.commons.exec.CommandLine;
//import org.apache.commons.exec.DefaultExecuteResultHandler;
//import org.apache.commons.exec.DefaultExecutor;
//import org.egc.sao.config.PathConfig;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.concurrent.Callable;
//
//public class CmdExecutor implements Callable<Integer> {
//
//    private String path;
//    private String resultDir;
//    CmdExecutor(String path,String resultDir){
//        this.path = path;
//        this.resultDir=resultDir;
//    }
//    @Override
//    public Integer call() throws Exception {
//        String line = "python "+ PathConfig.ANALYSIS_SCRIPT_PATH +" -ini "+this.path+"\\scenario_analysis.ini";
//        CommandLine cmdLine = CommandLine.parse(line);
//
//        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
//        DefaultExecutor executor = new DefaultExecutor();
//        executor.setExitValue(1);
//
//        executor.execute(cmdLine, resultHandler);
//        resultHandler.waitFor();
//
//        moveResultDirectory();
//        return resultHandler.getExitValue();
//    }
//    private void moveResultDirectory() throws IOException {
//        Files.move(new File(PathConfig.RESULT_PATH+"/"+this.resultDir).toPath(),
//                new File(this.path+"/result").toPath());
//    }
//}
