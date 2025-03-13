package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.BlockDto;

public interface BlockService {

    BlockDto addBlock(BlockDto blockDto);

    BlockDto updateBlock(Long blockId, BlockDto blockDto);

    BlockDto getBlockById(Long blockId);

    void deleteBlock(Long blockId);

    List<BlockDto> getAllBlocks();
    void createBlockB8();


}
