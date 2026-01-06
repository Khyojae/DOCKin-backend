package com.DOCKin.repository;

import com.DOCKin.model.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByUserId(String userId);
    List<Member> findByShipYardArea(String shipYardArea);

    Optional<Member> findByEmail(String email);
    List<Member> findByName(String name);


    //String userId(String userId);

    //List<Member> userId(String userId);
}
