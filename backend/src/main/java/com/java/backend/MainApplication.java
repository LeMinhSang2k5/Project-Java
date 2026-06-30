package com.java.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);


	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("-------------APP RUNNINGGGGGG--------------");
		logger.info("Active profiles: " + String.join(",", env.getActiveProfiles()));
		logger.info("spring.datasource.url=" + env.getProperty("spring.datasource.url"));
		System.out.println(
				"spring.datasource.driver-class-name=" + env.getProperty("spring.datasource.driver-class-name"));
	}
}