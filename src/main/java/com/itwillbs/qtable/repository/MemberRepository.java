package com.itwillbs.qtable.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	
    // member_type 기준으로 member 리스트를 찾기
    List<Member> findByMemberType(String member_type);
    
    // member_type이 type1 또는 type2 와 일치하는 member 리스트 + member_idx 내림차순
    List<Member> findByMemberTypeOrMemberTypeOrderByMemberIdxDesc(String type1, String type2);

	//사용자 로그인할때 필요함(덕교)
	Optional<Member> findByMemberId(String memberId) ; 
}
