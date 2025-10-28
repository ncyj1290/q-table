package com.itwillbs.qtable.repository;

import java.time.LocalDateTime;
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
	
	// 스케줄러 회원 3개월 이상 지났을시 삭제하는 메서드
	List<Member> findByMemberStatusAndLeaveAtBefore(String memberStatus, LocalDateTime leaveAt);
	// 맴버 이메일 중복 체크 exists 
	boolean existsByEmail(String email);
	
	// member_name, member_email 기준으로 회원이 존재하는지 조회
	Optional<Member> findByMemberNameAndEmail(String memberName, String memberEmail);
	
	// member_name을 기준으로 회원정보 조회
	Optional<Member> findByEmail(String email);

	// member_id, member_email 기준으로 회원이 존재하는지 조회
	Optional<Member> findByMemberIdAndEmail(String MemberId, String memberEmail);
}
