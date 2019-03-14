package org.egc.sao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.egc.sao.dao.postgresql")
public class SaosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaosApplication.class, args);
	}

}

