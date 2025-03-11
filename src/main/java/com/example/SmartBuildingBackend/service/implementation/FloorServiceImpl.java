package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.SmartBuildingBackend.dto.FloorDto;
import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.entity.Floor;
import com.example.SmartBuildingBackend.mapper.FloorMapper;
import com.example.SmartBuildingBackend.repository.BlockRepository;
import com.example.SmartBuildingBackend.repository.FloorRepository;
import com.example.SmartBuildingBackend.service.FloorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {
    private final FloorRepository floorRepository;
    private final BlockRepository blockRepository; // To find associated block
    @Override
    public List<FloorDto> getAllFloors() {
        List<Floor> floors = floorRepository.findAll();
        return floors.stream()
                .map(FloorMapper::mapToFloorDto)
                .collect(Collectors.toList());
    }

    @Override
    public FloorDto getFloorById(int floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));
        return FloorMapper.mapToFloorDto(floor);
    }

    @Override
    public FloorDto addFloor(int blockId, FloorDto floorDto) {
        // ✅ Ensure block exists before associating with a floor
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block not found with id: " + blockId));

        // ✅ Create new floor and set properties
        Floor floor = new Floor();
        floor.setFloor_name(floorDto.getFloorName());
        floor.setBlock(block); // Associate floor with the block

        // ✅ Save the floor and return response
        Floor savedFloor = floorRepository.save(floor);
        return FloorMapper.mapToFloorDto(savedFloor);
        
    }

    @Override
    public FloorDto updateFloor(int floorId, FloorDto updateFloor) {
        // ✅ Find existing floor
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));

        // ✅ Update floor name
        floor.setFloor_name(updateFloor.getFloorName());

        // ✅ Only update Block if a new block_id is provided
        if (updateFloor.getBlock_id() != null) {
            Block block = blockRepository.findById(updateFloor.getBlock_id())
                    .orElseThrow(() -> new RuntimeException("Block not found with id: " + updateFloor.getBlock_id()));
            floor.setBlock(block);
        }
        // ✅ Save updated floor and return DTO
        Floor updatedFloor = floorRepository.save(floor);
        return FloorMapper.mapToFloorDto(updatedFloor);
    }

    @Override
    public void deleteFloor(int floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));
        floorRepository.delete(floor);
    }
}
