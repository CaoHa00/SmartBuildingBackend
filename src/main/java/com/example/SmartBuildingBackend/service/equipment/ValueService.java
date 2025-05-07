package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.equipment.ValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Value;
public interface ValueService {

    ValueDto addValue(ValueDto valueDto);

    void deleteValue(UUID valueId);

    List<ValueDto> getAllValues();

    ValueDto getValueById(UUID valueId);

    ValueDto updateValue(UUID valueId, ValueDto valueDto);

    Value getValueByName(String nameValue);

    List<Value> getCachedValues();
}