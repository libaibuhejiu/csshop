package com.huaruan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huaruan.csshop.dao")
public class CsshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsshopApplication.class, args);
	}

}
