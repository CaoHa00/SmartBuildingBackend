package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SmartBuildingBackend.dto.ValueDto;
import com.example.SmartBuildingBackend.entity.Value;
import com.example.SmartBuildingBackend.mapper.ValueMapper;
import com.example.SmartBuildingBackend.repository.ValueRepository;
import com.example.SmartBuildingBackend.service.ValueService;

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
    public void deleteValue(Long valueId) {
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
    public ValueDto getValueById(Long valueId) {
        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));
        return ValueMapper.mapToValueDto(value);
    }

    @Override
    public ValueDto updateValue(Long valueId, ValueDto valueDto) {
        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));

        value.setValueName(valueDto.getValueName());

        Value updatedValue = valueRepository.save(value);
        return ValueMapper.mapToValueDto(updatedValue);
    }

    @Override
    public Long getValueByName(String nameValue) {
        List<Value> values = valueRepository.findAll();
        for(Value i:values){
            if(i.getValueName().equalsIgnoreCase(nameValue)){
                return i.getValueId();
            };
        }
       return null;
    }
}
