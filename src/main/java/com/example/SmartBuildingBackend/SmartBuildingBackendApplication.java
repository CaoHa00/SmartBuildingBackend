package com.example.SmartBuildingBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class SmartBuildingBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartBuildingBackendApplication.class, args);
		
	}
}