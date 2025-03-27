package com.example.SmartBuildingBackend.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "AqaraConfig")
@NoArgsConstructor
@AllArgsConstructor
public class AqaraConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aqara_config_id")
    private Long aqaraConfigId;
    @Column(name = "url")
    private String url;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "key_id")
    private String keyId;
    @Column(name = "app_key")
    private String appKey;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "expires_in")
    private String expiresIn;
    @Column(name = "open_id")
    private String opendId;
}
