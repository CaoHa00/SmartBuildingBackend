package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.AqaraConfigDto;
import com.example.SmartBuildingBackend.entity.AqaraConfig;
import com.example.SmartBuildingBackend.mapper.AqaraConfigMapper;
import com.example.SmartBuildingBackend.repository.AqaraConfigRepository;
import com.example.SmartBuildingBackend.service.AqaraConfigService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AqaraConfigImplementation implements AqaraConfigService{
    @Autowired
    private AqaraConfigRepository aqaraConfigRepository;

    @Override
    public AqaraConfigDto addAqaraConfig(AqaraConfigDto aqaraConfigDto) {
        AqaraConfig aqaraConfig  = AqaraConfigMapper.mapToAqaraConfig(aqaraConfigDto);
        AqaraConfig saveAqaraConfig = aqaraConfigRepository.save(aqaraConfig);
        return AqaraConfigMapper.mapToAqaraConfigDto(saveAqaraConfig);
    }

    @Override
    public AqaraConfigDto updateAqaraConfig(Long aqaraConfigId, AqaraConfigDto aqaraConfigDto) {
        AqaraConfig aqaraConfig = aqaraConfigRepository.findById(aqaraConfigId)
                .orElseThrow(() -> new RuntimeException("AqaraConfig is not found:" + aqaraConfigId));
        aqaraConfig.setAppId(aqaraConfigDto.getAppId());
        aqaraConfig.setAppKey(aqaraConfigDto.getAppKey());
        aqaraConfig.setAccessToken(aqaraConfigDto.getAccessToken());
        aqaraConfig.setExpiresIn(aqaraConfigDto.getExpiresIn());
        aqaraConfig.setRefreshToken(aqaraConfigDto.getRefreshToken());
        aqaraConfig.setOpendId(aqaraConfigDto.getOpendId());
        aqaraConfig.setUrl(aqaraConfig.getUrl());
        aqaraConfig.setKeyId(aqaraConfigDto.getKeyId());
        AqaraConfig updatedAqaraConfig = aqaraConfigRepository.save(aqaraConfig);
        return AqaraConfigMapper.mapToAqaraConfigDto(updatedAqaraConfig);
    }

    @Override
    public AqaraConfigDto getAqaraConfigById(Long aqaraConfigId) {
        AqaraConfig aqaraConfig = aqaraConfigRepository.findById(aqaraConfigId)
               .orElseThrow(() -> new RuntimeException("AqaraConfig is not found:" + aqaraConfigId));
        return AqaraConfigMapper.mapToAqaraConfigDto(aqaraConfig);
    }

    @Override
    public void deleteAqaraConfig(Long aqaraConfigId) {
      AqaraConfig aqaraConfig = aqaraConfigRepository.findById(aqaraConfigId)
              .orElseThrow(() -> new RuntimeException("AqaraConfig is not found:" + aqaraConfigId));
        aqaraConfigRepository.delete(aqaraConfig);
    }

    @Override
    public List<AqaraConfigDto> getAllAqaraConfigs() {
        List<AqaraConfig> aqaraConfigs = aqaraConfigRepository.findAll();
        return aqaraConfigs.stream()
                .map(AqaraConfigMapper::mapToAqaraConfigDto)
                .toList();
    }

    @Override
    public AqaraConfigDto refreshAccessToken(String accessToken, String refreshToken, String expirseIn, Long aqaraConfigId) {
        AqaraConfig aqaraConfig = aqaraConfigRepository.findById(aqaraConfigId)
                .orElseThrow(() -> new RuntimeException("AqaraConfig is not found:" + aqaraConfigId));
        aqaraConfig.setAccessToken(accessToken);
        aqaraConfig.setExpiresIn(expirseIn);
        aqaraConfig.setRefreshToken(refreshToken);
        AqaraConfig updatedAqaraConfig = aqaraConfigRepository.save(aqaraConfig);
        return AqaraConfigMapper.mapToAqaraConfigDto(updatedAqaraConfig);
        
    }

 
    
}
