package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.BlockDto;
import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.mapper.BlockMapper;
import com.example.SmartBuildingBackend.repository.BlockRepository;
import com.example.SmartBuildingBackend.service.BlockService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlockServiceImplementation implements BlockService {
    private BlockRepository blockRepository;

    @Override
    public BlockDto addBlock(BlockDto blockDto) {
        Block block = BlockMapper.mapToBlock(blockDto);
        Block saveBlock = blockRepository.save(block);
        return BlockMapper.mapToBlockDto(saveBlock);
    }

    @Override
    public BlockDto updateBlock(int blockId, BlockDto blockDto) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        block.setBlockName(blockDto.getBlockName());
        // block.setFloors(blockDto.getFloors());
        Block updatedBlock = blockRepository.save(block);
        return BlockMapper.mapToBlockDto(updatedBlock);
    }

    @Override
    public BlockDto getBlockById(int blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        return BlockMapper.mapToBlockDto(block);
    }

    @Override
    public void deleteBlock(int blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Block is not found:" + blockId));
        blockRepository.delete(block);
    }

    @Override
    public List<BlockDto> getAllBlocks() {
        List<Block> blocks = blockRepository.findAll();
        return blocks.stream().map(BlockMapper::mapToBlockDto).toList();
    }

}