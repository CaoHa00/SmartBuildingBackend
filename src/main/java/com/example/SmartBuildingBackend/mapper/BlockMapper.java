package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.BlockDto;
import com.example.SmartBuildingBackend.entity.Block;

public class BlockMapper {
    public static BlockDto mapToBlockDto(Block block) {
        return new BlockDto(
                block.getBlockId(),
                block.getBlockName(),
                block.getFloors());
    }

    public static Block mapToBlock(BlockDto blockDto) {
        return new Block(
                blockDto.getBlockId(),
                blockDto.getBlockName(),
                blockDto.getFloors());
    }
}
