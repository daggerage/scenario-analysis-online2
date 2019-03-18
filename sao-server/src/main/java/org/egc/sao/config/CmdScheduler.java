//package org.egc.sao.config;
//
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.*;
//
//@Configuration
//@EnableAsync
//public class CmdScheduler implements AsyncConfigurer {
//
//    private ThreadPoolExecutor pool;
//    private ExecutorService es;
//    private CmdScheduler() {
//        this.es=Executors.newFixedThreadPool(1);
//    }
//    private static class Singleton {
//        private final static CmdScheduler instance = new CmdScheduler();
//    }
//    public static CmdScheduler getInstance(){
//        return Singleton.instance;
//    }
//
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(1);
//        executor.setMaxPoolSize(1);
//        executor.setQueueCapacity(100);
//        executor.initialize();
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return null;
//    }
//    public void addTask(String path, String resultDir){
//        this.es.submit(new CmdExecutor(path,resultDir));
//    }
//}
