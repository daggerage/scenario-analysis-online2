package org.egc.sao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathConfig {
    @Value("${path.result-path}")
    public String resultPath;

    @Value("${path.analysis-script-path}")
    public String analysisResultPath;

    public static String PROJECT_PATH=System.getProperty("user.dir");
    public static String SEP="\\";
}
