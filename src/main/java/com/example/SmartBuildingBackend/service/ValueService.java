package com.example.SmartBuildingBackend.service;

import java.util.List;
import com.example.SmartBuildingBackend.dto.ValueDto;
public interface ValueService {

    ValueDto addValue(ValueDto valueDto);

    void deleteValue(Long valueId);

    List<ValueDto> getAllValues();

    ValueDto getValueById(Long valueId);

    ValueDto updateValue(Long valueId, ValueDto valueDto);
}