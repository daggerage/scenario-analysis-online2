package org.egc.sao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    @Value("${path.seims-root}")
    public static String SEIMS;

    @Value("${path.data}")
    public static String DATA;

    public static String MODEL_PATH;
    public static String ANALYSIS_SCRIPT_PATH;
//    public static String SEP= File.separator;
    public static String SEP= "/";
    public static String PROJECT_PATH=System.getProperty("user.dir").replace("\\",PathConfig.SEP);


    @Value("${path.seims-root}")
    public void setSEIMS(String seims) {
        SEIMS = getSepReplaced(seims);
    }

    @Value("${path.data}")
    public void setDATA(String data) {
        DATA = data;
    }

    private static String getSepReplaced(String path){
        return path.replace("/",PathConfig.SEP);
    }

    @Value("${path.seims-root}")
    public void setModelPath(String seims){
        MODEL_PATH = String.format("%s%sdata%syouwuzhen%sdemo_youwuzhen30m_longterm_model",
                getSepReplaced(seims),
                PathConfig.SEP,
                PathConfig.SEP,
                PathConfig.SEP);
    }
    @Value("${path.seims-root}")
    public void setAnalysisScriptPath(String seims){
        ANALYSIS_SCRIPT_PATH = String.format("%s%sseims%sscenario_analysis%sspatialunits%smain_nsga2.py",
                getSepReplaced(seims),
                PathConfig.SEP,
                PathConfig.SEP,
                PathConfig.SEP,
                PathConfig.SEP);
    }
}
