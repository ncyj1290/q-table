package com.itwillbs.qtable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.qtable.entity.Member;

public interface UserRepository extends JpaRepository<Member, Long> {
	 boolean existsByMemberId(String memberId);
}
