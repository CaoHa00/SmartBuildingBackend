package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.EquipmentTypeDto;
import com.example.SmartBuildingBackend.entity.EquipmentType;
import com.example.SmartBuildingBackend.mapper.EquipmentTypeMapper;
import com.example.SmartBuildingBackend.repository.EquipmentTypeRepository;
import com.example.SmartBuildingBackend.service.EquipementTypeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class EquipmentTypeImplementation implements EquipementTypeService {
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Override
    public EquipmentTypeDto addEquipmentType(EquipmentTypeDto equipmentTypeDto) {
        EquipmentType equipementType = EquipmentTypeMapper.mapToEquipmentType(equipmentTypeDto);
        EquipmentType savEquipmentType = equipmentTypeRepository.save(equipementType);
        return EquipmentTypeMapper.mapToEquipmentTypeDto(savEquipmentType);

    }

    @Override
    public EquipmentTypeDto updateEquipmentTypeDto(EquipmentTypeDto equipmentTypeDto, UUID Id) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Equipement type is not found:" + Id));
        equipmentType.setEquipmentTypeName(equipmentTypeDto.getEquipmentTypeName());
        equipmentType.setEquipments(equipmentTypeDto.getEquipments());
        EquipmentType updatedEquipmentType = equipmentTypeRepository.save(equipmentType);
        return EquipmentTypeMapper.mapToEquipmentTypeDto(updatedEquipmentType);
    }

    @Override
    public EquipmentTypeDto getEquipmentTypeById(UUID Id) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Equipement type is not found:" + Id));
        return EquipmentTypeMapper.mapToEquipmentTypeDto(equipmentType);
    }

    @Override
    public void deleteEquipementType(UUID Id) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Equipement type is not found:" + Id));
        equipmentTypeRepository.delete(equipmentType);
    }

    @Override
    public List<EquipmentTypeDto> getAllEquipmentType() {
        List<EquipmentType> equipmentTypes = equipmentTypeRepository.findAll();
        return equipmentTypes.stream().map(EquipmentTypeMapper::mapToEquipmentTypeDto).toList();

    }

}
