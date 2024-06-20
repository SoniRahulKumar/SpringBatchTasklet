package com.springbatchtasklet.springbatchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableBatchProcessing
//@EnableJpaRepositories("com.springbatchtasklet.springbatchtasklet.repository")
public class SpringBatchTaskletApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTaskletApplication.class, args);
	}


}
