package com.example.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackageClasses = HotPlerApiApplication.class)
@SpringBootApplication
public class HotPlerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotPlerApiApplication.class, args);
	}

}
