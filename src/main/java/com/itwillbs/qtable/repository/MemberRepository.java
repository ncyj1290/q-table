package com.itwillbs.qtable.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	//사용자 로그인할때 필요함(덕교)
	Optional<Member> findByMemberId(String memberId) ; 
}
