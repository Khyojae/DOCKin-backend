package com.DOCKin.ai.repository;

import com.DOCKin.ai.model.TranslateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslateRepository extends JpaRepository<TranslateLog,Long> {
}
