package com.DOCKin.repository;

import com.DOCKin.model.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member,String> {
}
