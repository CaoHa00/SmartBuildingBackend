package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.BlockDto;

public interface BlockService {

    BlockDto addBlock(BlockDto blockDto);

    BlockDto updateBlock(int blockId, BlockDto blockDto);

    BlockDto getBlockById(int blockId);

    void deleteBlock(int blockId);

    List<BlockDto> getAllBlocks();
    


}
