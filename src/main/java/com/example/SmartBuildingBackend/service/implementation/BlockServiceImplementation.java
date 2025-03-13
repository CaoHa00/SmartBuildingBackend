package com.example.SmartBuildingBackend.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.BlockDto;

import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.entity.Floor;
import com.example.SmartBuildingBackend.entity.Room;
import com.example.SmartBuildingBackend.mapper.BlockMapper;
import com.example.SmartBuildingBackend.repository.BlockRepository;
import com.example.SmartBuildingBackend.service.BlockService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlockServiceImplementation implements BlockService {
    @Autowired
    private BlockRepository blockRepository;

    @Override
    public BlockDto addBlock(BlockDto blockDto) {
        Block block = BlockMapper.mapToBlock(blockDto);
        Block saveBlock = blockRepository.save(block);
        return BlockMapper.mapToBlockDto(saveBlock);
    }

    @Override
    public BlockDto updateBlock(Long blockId, BlockDto blockDto) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        block.setBlockName(blockDto.getBlockName());
        // block.setFloors(blockDto.getFloors());
        Block updatedBlock = blockRepository.save(block);
        return BlockMapper.mapToBlockDto(updatedBlock);
    }

    @Override
    public BlockDto getBlockById(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        return BlockMapper.mapToBlockDto(block);
    }

    @Override
    public void deleteBlock(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        blockRepository.delete(block);
    }

    @Override
    public List<BlockDto> getAllBlocks() {
        List<Block> blocks = blockRepository.findAll();
        return blocks.stream().map(BlockMapper::mapToBlockDto).toList();
    }

    @Override
    @Transactional
    public void createBlockB8() {
          Block block = Block.builder().blockName("B8").build();
        List<Floor> floorList = new ArrayList<>();

        for (int floorNum = 1; floorNum <= 4; floorNum++) {
            Floor floor = Floor.builder().floorName(String.valueOf(floorNum)).block(block).build();
            List<Room> roomList = new ArrayList<>();

            for (int roomNum = 1; roomNum <= 23; roomNum++) {
                String roomName = floorNum + String.format("%02d", roomNum);
                Room room = Room.builder().roomName(roomName).floor(floor).build();
                roomList.add(room);
            }

            floor.setRooms(roomList);
            floorList.add(floor);
        }
        block.setFloors(floorList);
        blockRepository.save(block);
    }
}