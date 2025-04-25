package com.example.SmartBuildingBackend.mapper.equipment;

import com.example.SmartBuildingBackend.dto.equipment.ValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Value;

public class ValueMapper {
    public static ValueDto mapToValueDto(Value value) {
        return new ValueDto(
                value.getValueId(),
                value.getValueName()
        );
    }

    public static Value mapToValue(ValueDto valueDto) {
        return new Value(
                valueDto.getValueId(),
                valueDto.getValueName()
        );
    }
}
