package org.egc.sao;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class SaosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaosApplication.class, args);
	}

//	@Bean
//	public Jackson2ObjectMapperBuilder jacksonBuilder() {
//		Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
//		b.propertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
//		return b;
//	}
}

