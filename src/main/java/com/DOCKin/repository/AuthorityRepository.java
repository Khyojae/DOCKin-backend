package com.DOCKin.repository;

import com.DOCKin.model.Authority;
import com.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    List<Authority> findByMember(Member member);
}
