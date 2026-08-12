package com.chanu.taskmanager.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chanu.taskmanager.entity.TaskAuditLedger;
import com.chanu.taskmanager.repository.TaskAuditLedgerRepository;

@Service
public class BlockchainService {

    private static final String GENESIS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    private final TaskAuditLedgerRepository ledgerRepository;

    public BlockchainService(TaskAuditLedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public synchronized TaskAuditLedger recordBlock(Long taskId, String action) {
        Optional<TaskAuditLedger> lastBlock = ledgerRepository.findTopByOrderByIdDesc();
        String previousHash = lastBlock.map(TaskAuditLedger::getBlockHash).orElse(GENESIS_HASH);

        LocalDateTime now = LocalDateTime.now();
        String dataToHash = taskId + ":" + action + ":" + now.toString() + ":" + previousHash;
        String blockHash = calculateSha256(dataToHash);

        TaskAuditLedger block = new TaskAuditLedger(taskId, action, previousHash, blockHash);
        block.setTimestamp(now);
        return ledgerRepository.save(block);
    }

    public List<TaskAuditLedger> getFullLedger() {
        return ledgerRepository.findAllByOrderByIdAsc();
    }

    public Map<String, Object> verifyChainIntegrity() {
        List<TaskAuditLedger> ledger = getFullLedger();
        boolean isValid = true;
        String message = "Blockchain audit trail is 100% valid and untampered.";

        for (int i = 0; i < ledger.size(); i++) {
            TaskAuditLedger currentBlock = ledger.get(i);
            String expectedPrevHash = (i == 0) ? GENESIS_HASH : ledger.get(i - 1).getBlockHash();

            if (!currentBlock.getPreviousHash().equals(expectedPrevHash)) {
                isValid = false;
                message = "Block #" + currentBlock.getId() + " previous hash mismatch!";
                break;
            }

            String recomputedHash = calculateSha256(
                currentBlock.getTaskId() + ":" +
                currentBlock.getAction() + ":" +
                currentBlock.getTimestamp().toString() + ":" +
                currentBlock.getPreviousHash()
            );

            if (!currentBlock.getBlockHash().equals(recomputedHash)) {
                isValid = false;
                message = "Block #" + currentBlock.getId() + " hash tampered!";
                break;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("valid", isValid);
        result.put("message", message);
        result.put("totalBlocks", ledger.size());
        return result;
    }

    private String calculateSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
