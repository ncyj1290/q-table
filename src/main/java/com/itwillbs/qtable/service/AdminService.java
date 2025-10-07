package com.itwillbs.qtable.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;

import jakarta.transaction.Transactional;

@Service
public class AdminService {

	@Autowired
	private MemberRepository memberRepository;

	// 전체 회원 목록 조회
	public List<MemberListVO> memberFindAll() {

		// jpa findAll
		List<Member> memberList = memberRepository.findAll();
		System.out.println("memberList : " + memberList);

		return memberList.stream().map(entity -> new MemberListVO(entity, entity.getMemberIdx()))
				.collect(Collectors.toList());
	}

	// 회원 상세정보 조회
	public MemberDetailVO findMemberDetailById(Integer memberIdx) {
		// DB에서 해당 회원 findById로 찾기
		Member memberEntity = memberRepository.findById(memberIdx).orElseThrow();

		// MemberDetailVO로 변환하여 반환
		return new MemberDetailVO(memberEntity);
	}

	// 회원 상태 변경
	@Transactional
	public void memberUpdate(Integer memberIdx, MemberUpdateVO MemberUpdateVO) {
		// DB에서 해당 회원 findById로 찾기
		Member member = memberRepository.findById(memberIdx).orElseThrow();

		String newStatus = MemberUpdateVO.getMember_status();

		member.setMemberStatus(newStatus);

		// member_status가 정상인지 확인
		if ("mstat_01".equals(newStatus)) {
			// 정상일 경우, 탈퇴 사유를 null
			member.setLeaveReason(null);
		} else {
			// 탈퇴일 경우 탈퇴 사유를 저장
			member.setLeaveReason(MemberUpdateVO.getLeave_reason());
		}

	}

}
