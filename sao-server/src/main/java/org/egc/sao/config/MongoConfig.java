package org.egc.sao.config;

import com.mongodb.MongoClient;
import org.egc.sao.dao.mongodb.StructBMPDao;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = StructBMPDao.class)
public class MongoConfig extends AbstractMongoConfiguration {
    private static String HOST;
    private static Integer PORT;
    private static String DB_NAME;


    @Value("${spring.data.mongodb.host}")
    public void setIP(String host){
        HOST = host;
    }

    @Value("${spring.data.mongodb.port}")
    public void setPort(Integer port){
        PORT = port;
    }

    @Value("${spring.data.mongodb.scenario-db-name}")
    public void setDBName(String name){
        DB_NAME = name;
    }

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(HOST,PORT);
    }

    @Override
    protected String getMappingBasePackage() {
        return "org.egc.sao.dao.mongodb";
    }


    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        //Important, map camelCase field names to ALL_UPPER_CASE in domain of mongodb.
        //TODO: specify the Class or package applies this stratety, or do this by annotaion @UpperCaseNaming
        context.setFieldNamingStrategy(new UpperCaseWithUnderscoreFieldNamingStrategy());

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        } catch (NoSuchBeanDefinitionException ignore) {
        }

        // Don't save _class to mongo
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }
}
