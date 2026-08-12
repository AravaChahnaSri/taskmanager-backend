package com.chanu.taskmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chanu.taskmanager.entity.TaskAuditLedger;

public interface TaskAuditLedgerRepository extends JpaRepository<TaskAuditLedger, Long> {
    Optional<TaskAuditLedger> findTopByOrderByIdDesc();
    List<TaskAuditLedger> findAllByOrderByIdAsc();
}
