package com.itwillbs.qtable.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.mapper.admin.AdminMapper;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.repository.StoreRepository;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;
import com.itwillbs.qtable.vo.admin.StoreDetailVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;
import com.itwillbs.qtable.vo.admin.StoreUpdateVO;

import jakarta.transaction.Transactional;

@Service
public class AdminService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
    private AdminMapper adminMapper;

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
	
    // member_idx로 매장 상세 정보를 조회
    public StoreDetailVO findByMemberIdx(Integer member_idx) {

        return storeRepository.findByMemberIdx(member_idx)
                .map(StoreDetailVO::new)
                .orElse(null);
    }

	// 회원 상태 변경
	@Transactional
	public void memberUpdate(Integer memberIdx, MemberUpdateVO MemberUpdateVO) {
		// DB에서 해당 회원 findById로 찾기
		Member member = memberRepository.findById(memberIdx).orElseThrow();

		String newStatus = MemberUpdateVO.getMember_status();

		member.setMemberStatus(newStatus);
		member.setLeaveAt(LocalDateTime.now()); // 처리 시각 기록

		// member_status가 정상인지 확인
		if ("mstat_01".equals(newStatus)) {
			// 정상일 경우, 탈퇴 사유를 null
			member.setLeaveReason(null);
			member.setLeaveAt(null);
		} else {
			// 탈퇴일 경우 탈퇴 사유를 저장
			member.setLeaveReason(MemberUpdateVO.getLeave_reason());
		}

	}
	
	// 회원 삭제 이벤트
    @Transactional
    public void deleteMember(Integer memberIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
        memberRepository.deleteById(memberIdx);
    }
    
    // --------------------- 매장 ------------------
    
    // 매장 회원 목록 리스트 조회
    public List<StoreListVO> findStoreMembers() {
        return adminMapper.findStoreMembers();
    }
    
    // 매장 입점 신청 목록 리스트 조회
    public List<StoreListVO> findEntryStores() {
        return adminMapper.findEntryStores();
    }
    
    // 매장 상세 정보 조회
    public StoreDetailVO findStoreDetailById(Integer storeIdx) {
        Store store = storeRepository.findById(storeIdx).orElseThrow();
        return new StoreDetailVO(store);
    }

    // 매장 상태 업데이트
    @Transactional
    public void updateStoreStatus(Integer storeIdx, StoreUpdateVO StoreUpdateVO) {
        Store store = storeRepository.findById(storeIdx).orElseThrow();
        String newStatus = StoreUpdateVO.getStore_status();
        store.setStoreStatus(newStatus);
        store.setProcessedAt(LocalDateTime.now()); // 처리 시각 기록
        
		// Store_status가 정상인지 확인
		if ("srst_01".equals(newStatus) || "srst_02".equals(newStatus)) {
			// 승인, 보류일 경우, 탈퇴 사유를 null
			store.setRejectionReason(null);;
		} else {
			// 거부일 경우 거부 사유를 저장
			store.setRejectionReason(StoreUpdateVO.getRejection_reason());
		}
		
    }
    
	// 매장 삭제 이벤트
    @Transactional
    public void deleteStore(Integer storeIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
        storeRepository.deleteById(storeIdx);
    }

}
