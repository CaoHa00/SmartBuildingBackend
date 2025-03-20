package com.example.SmartBuildingBackend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aqara.api")
public class AqaraConfig {
    
    private String url;

    
    private String accessToken;

   
    private String appId;

   
    private String keyId;

    
    private String appKey;
}
