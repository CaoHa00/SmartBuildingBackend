package com.example.SmartBuildingBackend.service.implementation.campusImplementation;

import com.example.SmartBuildingBackend.dto.campus.SpaceDto;
import com.example.SmartBuildingBackend.entity.campus.Space;
import com.example.SmartBuildingBackend.entity.campus.SpaceType;
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;
import com.example.SmartBuildingBackend.mapper.campus.SpaceMapper;
import com.example.SmartBuildingBackend.repository.campus.SpaceRepository;
import com.example.SmartBuildingBackend.repository.campus.SpaceTypeRepository;
import com.example.SmartBuildingBackend.service.campusService.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceServiceImplementation implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final SpaceTypeRepository spaceTypeRepository;

    @Override
    public SpaceDto createSpace(SpaceDto dto) {
        SpaceType spaceType = spaceTypeRepository.findById(dto.getSpaceTypeId())
                .orElseThrow(
                        () -> new NoSuchElementException("SpaceType with ID " + dto.getSpaceTypeId() + " not found"));

        Space parent = null;
        if (dto.getParentId() != null) {
            parent = spaceRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Parent Space with ID " + dto.getParentId() + " not found"));
        }

        Space space = SpaceMapper.mapToEntity(dto, spaceType, parent);
        Space saved = spaceRepository.save(space);

        return SpaceMapper.mapToDto(saved, new HashSet<>());
    }

    @Override
    public SpaceDto getSpaceById(UUID id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Space with ID " + id + " not found"));

        return SpaceMapper.mapToDto(space, new HashSet<>());
    }

    @Override
    public List<SpaceDto> getAllSpaces() {
        return spaceRepository.findAll().stream()
                .map(space -> SpaceMapper.mapToDto(space, new HashSet<>()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SpaceDto> getSpacesAsTree() {
        List<Space> allSpaces = spaceRepository.findAll();
        Map<UUID, SpaceDto> dtoMap = new HashMap<>();
        List<SpaceDto> roots = new ArrayList<>();

        for (Space space : allSpaces) {
            SpaceDto dto = new SpaceDto();
            dto.setSpaceId(space.getSpaceId());
            dto.setSpaceName(space.getSpaceName());
            dto.setSpaceTypeId(space.getSpaceType().getSpaceTypeId());
            dto.setSpaceTypeName(space.getSpaceType().getSpaceTypeName());
            dto.setParentId(space.getParent() != null ? space.getParent().getSpaceId() : null);
            dto.setChildren(new ArrayList<>());
            dto.setEquipments(
                    space.getEquipments() != null
                            ? space.getEquipments().stream()
                                    .map(EquipmentMapper::mapToEquipmentDto)
                                    .collect(Collectors.toList())
                            : new ArrayList<>());
            dtoMap.put(dto.getSpaceId(), dto);
        }

        for (SpaceDto dto : dtoMap.values()) {
            UUID parentId = dto.getParentId();
            if (parentId != null && dtoMap.containsKey(parentId)) {
                dtoMap.get(parentId).getChildren().add(dto);
            } else {
                roots.add(dto);
            }
        }

        roots.sort(Comparator.comparing(SpaceDto::getSpaceName));
        sortChildrenRecursively(roots);

        return roots;
    }

    private void sortChildrenRecursively(List<SpaceDto> list) {
        for (SpaceDto dto : list) {
            if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
                dto.getChildren().sort(Comparator.comparing(SpaceDto::getSpaceName));
                sortChildrenRecursively(dto.getChildren());
            }
        }
    }

    @Override
    public SpaceDto updateSpace(UUID id, SpaceDto dto) {
        Space existing = spaceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Space with ID " + id + " not found"));

        SpaceType spaceType = spaceTypeRepository.findById(dto.getSpaceTypeId())
                .orElseThrow(
                        () -> new NoSuchElementException("SpaceType with ID " + dto.getSpaceTypeId() + " not found"));

        Space parent = null;
        if (dto.getParentId() != null) {
            parent = spaceRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Parent Space with ID " + dto.getParentId() + " not found"));
        }

        existing.setSpaceName(dto.getSpaceName());
        existing.setSpaceType(spaceType);
        existing.setParent(parent);

        Space updated = spaceRepository.save(existing);
        return SpaceMapper.mapToDto(updated, new HashSet<>());
    }

    @Override
    public void deleteSpace(UUID id) {
        if (!spaceRepository.existsById(id)) {
            throw new NoSuchElementException("Space with ID " + id + " not found");
        }
        spaceRepository.deleteById(id);
    }
}
