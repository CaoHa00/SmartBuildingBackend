package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SmartBuildingBackend.dto.equipment.ValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Value;
import com.example.SmartBuildingBackend.mapper.equipment.ValueMapper;
import com.example.SmartBuildingBackend.repository.equipment.ValueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ValueServiceImplementation implements ValueService {

    private final ValueRepository valueRepository;

    @Override
    public ValueDto addValue(ValueDto valueDto) {
        Value value = ValueMapper.mapToValue(valueDto);
        Value savedValue = valueRepository.save(value);
        return ValueMapper.mapToValueDto(savedValue);
    }

    @Override
    public void deleteValue(UUID valueId) {
        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));
        valueRepository.delete(value);
    }

    @Override
    public List<ValueDto> getAllValues() {
        List<Value> values = valueRepository.findAll();
        return values.stream()
                .map(ValueMapper::mapToValueDto)
                .collect(Collectors.toList());
    }

    @Override
    public ValueDto getValueById(UUID valueId) {
        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));
        return ValueMapper.mapToValueDto(value);
    }

    @Override
    public ValueDto updateValue(UUID valueId, ValueDto valueDto) {
        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));

        value.setValueName(valueDto.getValueName());

        Value updatedValue = valueRepository.save(value);
        return ValueMapper.mapToValueDto(updatedValue);
    }

    @Override
    public Value getValueByName(String valueName) {
        // Make sure Optional is imported correctly
        return valueRepository.findByValueName(valueName);
    }

}
