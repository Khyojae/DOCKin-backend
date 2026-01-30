package com.DOCKin.ai.repository;

import com.DOCKin.ai.model.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog,Long> {
}
