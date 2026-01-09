package com.DOCKin.repository;

import com.DOCKin.model.Member;
import com.DOCKin.model.Work_logs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface Work_logsRepository extends JpaRepository<Work_logs, Long> {
    @Transactional
    void deleteAllByMember(Member member);

    List<Work_logs> findByMember(Member member);
    List<Work_logs> findByMemberIn(Collection<Member> members);
}
