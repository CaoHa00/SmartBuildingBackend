package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.AqaraConfigDto;
import com.example.SmartBuildingBackend.entity.AqaraConfig;

public class AqaraConfigMapper {
    
    public static AqaraConfigDto mapToAqaraConfigDto(AqaraConfig aqaraConfig) {
        return new AqaraConfigDto(
                aqaraConfig.getAqaraConfigId(),
                aqaraConfig.getUrl(),
                aqaraConfig.getAccessToken(),
                aqaraConfig.getAppId(),
                aqaraConfig.getKeyId(),
                aqaraConfig.getAppKey(),
                aqaraConfig.getRefreshToken(),
                aqaraConfig.getExpiresIn(),
                aqaraConfig.getOpendId()
                );
    }

    public static AqaraConfig mapToAqaraConfig(AqaraConfigDto aqaraConfigDto) {
        return new AqaraConfig(
                aqaraConfigDto.getAqaraConfigId(),
                aqaraConfigDto.getUrl(),
                aqaraConfigDto.getAccessToken(),
                aqaraConfigDto.getAppId(),
                aqaraConfigDto.getKeyId(),
                aqaraConfigDto.getAppKey(),
                aqaraConfigDto.getRefreshToken(),
                aqaraConfigDto.getExpiresIn(),
                aqaraConfigDto.getOpendId()
                );
    }
}
