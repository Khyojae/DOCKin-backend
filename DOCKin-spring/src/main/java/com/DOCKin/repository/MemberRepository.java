package com.DOCKin.repository;

import com.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,String> {
    List<Member> findByUserId(String userId);
    List<Member> findByShipYardArea(String shipYardArea);
}
