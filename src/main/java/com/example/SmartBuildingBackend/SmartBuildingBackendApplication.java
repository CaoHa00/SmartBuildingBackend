package com.example.SmartBuildingBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.SmartBuildingBackend.service.provider.tuya.TuyaProperties;
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(TuyaProperties.class)
public class SmartBuildingBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartBuildingBackendApplication.class, args);
		
	}
}