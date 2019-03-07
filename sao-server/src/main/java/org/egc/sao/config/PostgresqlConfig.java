//package org.egc.sao.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan(basePackages = PostgresqlConfig.PACKAGE)
//public class PostgresqlConfig {
//    static final String PACKAGE = "org.egc.sao.dao.postgresql";
//    static final String MAPPER_LOCATION="classpath:mapper/postgresql/*.xml";
//
//    @Value("${postgresql.datasource.uri}")
//    private String url;
//
//    @Value("${postgresql.datasource.user}")
//    private String user;
//
//    @Value("${postgresql.datasource.password}")
//    private String password;
//
//    @Value("${postgresql.datasource.driverClassName}")
//    private String driver;
//
//    @Bean(name = "postgresqlDataSource")
//    @Primary
//    public DataSource postgresqlDataSource(){
//        DruidDataSource dataSource = new DruidDataSource();
////        dataSource.setDriverClassName();
//        return null;
//    }
//
//    @Bean(name = "postgresqlDataSource")
//    @Primary
//    public DataSourceTransactionManager mongoTransactionManager(){
//        return new DataSourceTransactionManager();
//    }
//}
