package com.example.SmartBuildingBackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.BlockDto;
import com.example.SmartBuildingBackend.service.BlockService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/block")
public class BlockController {
    public final BlockService blockService;

    @GetMapping
    public ResponseEntity<List<BlockDto>> getAllBlocks() {
        List<BlockDto> blocks = blockService.getAllBlocks();
        return ResponseEntity.status(HttpStatus.OK).body(blocks);
    }

    @PostMapping
    public ResponseEntity<BlockDto> addBlock( @RequestBody BlockDto blockDto) {
        BlockDto newBlock = blockService.addBlock(blockDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBlock);
    }

    @GetMapping("/{block_id}")
    public ResponseEntity<BlockDto> getBlockById(@PathVariable("block_id") Long blockId) {
        BlockDto block = blockService.getBlockById(blockId);
        return ResponseEntity.ok(block);
    }

    @PutMapping("/{block_id}")
    public ResponseEntity<BlockDto> updateBlock(@PathVariable("block_id") Long blockId,
             @RequestBody BlockDto blockDto) {
        BlockDto updatedBlock = blockService.updateBlock(blockId, blockDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedBlock);
    }

    @DeleteMapping("/{block_id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable("block_id") Long blockId) {
        blockService.deleteBlock(blockId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/create-b8")
    public ResponseEntity<String> createBlockB8() {
        blockService.createBlockB8();
        return ResponseEntity.status(HttpStatus.CREATED).body("Block B8 created successfully!");
    }
}
