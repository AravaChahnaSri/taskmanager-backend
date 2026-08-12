package com.chanu.taskmanager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chanu.taskmanager.entity.TaskAuditLedger;
import com.chanu.taskmanager.service.BlockchainService;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/ledger")
    public ResponseEntity<List<TaskAuditLedger>> getLedger() {
        return ResponseEntity.ok(blockchainService.getFullLedger());
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyIntegrity() {
        return ResponseEntity.ok(blockchainService.verifyChainIntegrity());
    }
}
