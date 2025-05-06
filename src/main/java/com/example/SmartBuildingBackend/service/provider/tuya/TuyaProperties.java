package com.example.SmartBuildingBackend.service.provider.tuya;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tuya")

public class TuyaProperties {

    private String clientId;
    private String secret;
    private String baseUrl;

}