package com.ll.grabit.boundedcontext.member.repository;

import com.ll.grabit.boundedcontext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
}
