package com.example.SmartBuildingBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AqaraConfigDto {
    private Long aqaraConfigId;
    private String url;
    private String accessToken;
    private String appId;
    private String keyId;
    private String appKey;
    private String refreshToken;
    private String expiresIn;
    private String opendId;
}
