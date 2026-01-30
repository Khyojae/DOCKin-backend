package com.DOCKin.member.repository;

import com.DOCKin.member.model.Authority;
import com.DOCKin.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    List<Authority> findByMember(Member member);
}
