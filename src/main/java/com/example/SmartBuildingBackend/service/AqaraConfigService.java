package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.AqaraConfigDto;

public interface AqaraConfigService {
    AqaraConfigDto addAqaraConfig(AqaraConfigDto aqaraConfigDto);

    AqaraConfigDto updateAqaraConfig(Long aqaraConfigId, AqaraConfigDto aqaraConfigDto);

    AqaraConfigDto getAqaraConfigById(Long aqaraConfigId);

    void deleteAqaraConfig(Long aqaraConfigId);

    List<AqaraConfigDto> getAllAqaraConfigs();

    AqaraConfigDto  refreshAccessToken(String accessToken, String refreshToken, String expirseIn, Long aqaraConfigId);

}
