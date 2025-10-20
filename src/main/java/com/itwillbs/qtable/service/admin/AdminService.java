package com.itwillbs.qtable.service.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.config.MaskingUtils;
import com.itwillbs.qtable.entity.Jeongsan;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.mapper.admin.AdminMapper;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.repository.JeongsanRepository;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.repository.PaymentRepository;
import com.itwillbs.qtable.repository.StoreRepository;
import com.itwillbs.qtable.repository.SubscribeRepository;
import com.itwillbs.qtable.repository.UserLogRepository;
import com.itwillbs.qtable.vo.admin.JeongsanListVO;
import com.itwillbs.qtable.vo.admin.JeongsanUpdateVO;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;
import com.itwillbs.qtable.vo.admin.PaymentListVO;
import com.itwillbs.qtable.vo.admin.StoreDetailVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;
import com.itwillbs.qtable.vo.admin.StoreUpdateVO;
import com.itwillbs.qtable.vo.admin.SubscribeListVO;
import com.itwillbs.qtable.vo.admin.UserLogVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeGroupVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

import jakarta.transaction.Transactional;

@Service
public class AdminService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private JeongsanRepository jeongsanRepository;
	
	@Autowired
	private SubscribeRepository subscribeRepository;
	
	@Autowired
	private UserLogRepository userLogRepository;
	
	@Autowired
    private AdminMapper adminMapper;
	
	@Autowired
	private StoreData storedata;
	
	// 관리자 메인화면 일주일간 신규 회원
	public int countNewMembers() {
		return adminMapper.countNewMembers();
	}
	
	// 관리자 메인화면 일주일간 신규 매장회원
	public int countNewStoreMembers() {
		return adminMapper.countNewStoreMembers();
	}
	
	// 관리자 메인화면 1일 신규 입점 신청
	public int countNewStoreEntry() {
		return adminMapper.countNewStoreEntry();
	}

    // 전체 회원 목록 조회
    public List<MemberListVO> memberFindAll() {
    	
    	// 조회할 회원 타입 지정
        String type1 = "mtype_02"; // 일반회원
        String type2 = "mtype_03"; // 매장관리자

        // member_type이 type1 또는 type2 와 일치하는 member 리스트 + member_idx 내림차순
        List<Member> memberList = memberRepository.findByMemberTypeOrMemberTypeOrderByMemberIdxDesc(type1, type2);

        System.out.println("memberList : " + memberList);

        List<MemberListVO> memberVos = new ArrayList<>();

        for (Member entity : memberList) {
            MemberListVO vo = new MemberListVO(entity, entity.getMemberIdx()); 

            // --- 마스킹 로직 ---
            // 탈퇴 상태이고, 탈퇴일 존재하며, 3개월 이내일 때
            if ("mstat_02".equals(entity.getMemberStatus()) &&
                entity.getLeaveAt() != null &&
                entity.getLeaveAt().isAfter(LocalDateTime.now().minusMonths(3)))
            {
                vo.setMember_name(MaskingUtils.maskName(entity.getMemberName())); // 별도의 MaskingUtils 클래스로 분리
                vo.setEmail(MaskingUtils.maskEmail(entity.getEmail()));
            }
            // --- 마스킹 로직 끝 ---

            memberVos.add(vo);
        }

        return memberVos;
    }

	// 회원 상세정보 조회
	public MemberDetailVO findMemberDetailById(Integer memberIdx) {
		// DB에서 해당 회원 findById로 찾기
		Member memberEntity = memberRepository.findById(memberIdx).orElseThrow();

		// MemberDetailVO로 변환하여 반환
		return new MemberDetailVO(memberEntity);
	}
	
	public MemberDetailVO findMemberDetail(Integer member_idx) {
		
		MemberDetailVO vo = adminMapper.findMemberDetail(member_idx);

        if (vo != null) {
            // -- 마스킹 로직
            // 탈퇴 상태이고, 탈퇴일 존재하며, 3개월 이내일 때
             if ("mstat_02".equals(vo.getMember_status()) && // 코드 값 필드 사용
                 vo.getLeave_at() != null &&
                 vo.getLeave_at().isAfter(LocalDateTime.now().minusMonths(3)))
             {
                 vo.setMember_name(MaskingUtils.maskName(vo.getMember_name())); // 별도 MaskingUtils 유틸리티 클래스로 분리
                 vo.setEmail(MaskingUtils.maskEmail(vo.getEmail()));
                 vo.setAddress("탈퇴 회원 주소");
                 vo.setAddress_detail("");
                 vo.setBirth(null);
                 vo.setPost_code("*****");
                 vo.setStore_name("탈퇴한 매장명");
                 vo.setAccount_number("탈퇴 회원 계좌번호");
                 vo.setBusiness_reg_no("탈퇴한 사업자등록번호");
             }
        }
        return vo;
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

		// member_status가 정상인지 확인
		if ("mstat_02".equals(newStatus)) {
			// 정상일 경우, 탈퇴 사유를 null
			member.setLeaveReason(MemberUpdateVO.getLeave_reason());
			member.setLeaveAt(LocalDateTime.now());
		} else {
			// 탈퇴일 경우 탈퇴 사유를 저장
			member.setLeaveReason(null);
			member.setLeaveAt(null);
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
			// 승인, 보류일 경우, 반려 사유를 null
			store.setRejectionReason(null);;
		} else {
			// 반려일 경우 반려 사유를 저장
			store.setRejectionReason(StoreUpdateVO.getRejection_reason());
		}
		
    }
    
    @Transactional
    public void deleteStoreMember(Integer member_idx) {
    	
        // store_idx를 찾기
        Integer store_idx = storeRepository.findByMemberIdx(member_idx)
                                .map(Store::getStoreIdx)
                                .orElse(null);

        // store_idx가 존재할 경우 하위 데이터를 삭제
        if (store_idx != null) {
        	storedata.deleteHolidayByStoreIdx(store_idx);
        	storedata.deleteCategoryByStoreIdx(store_idx);
        	storedata.deleteAmenityByStoreIdx(store_idx);
        	storedata.deleteAtmosphereByStoreIdx(store_idx);
        	storedata.deleteIngredientByStoreIdx(store_idx);
        	storedata.deleteMenuByStoreIdx(store_idx);
        	
        	// 매장과 관련된 이미지 타입들을 배열로 정의
            String[] imageTypesToDelete = {"imguse_01", "imguse_02", "imguse_03", "imguse_04", "imguse_05"};

            // 각 이미지 타입에 대해 삭제 메소드 호출
            for (String imageType : imageTypesToDelete) {
            	storedata.deleteStoreImageByStoreIdx(store_idx, imageType);
            }

            // store 테이블의 데이터 삭제(JPA 사용)
            storeRepository.deleteById(store_idx);
        }
        
        // member 테이블의 데이터 삭제(JPA 사용)
        memberRepository.deleteById(member_idx);
    }
    
    // -------------------------- 결제 -----------------------
    
    // 회원 결제 목록 리스트 조회
    public List<PaymentListVO> findPaymentListMembers() {
        return adminMapper.findPaymentListMembers();
    }
    
    // 매장 결제 목록 리스트 조회
    public List<PaymentListVO> findPaymentListStores() {
        return adminMapper.findPaymentListStores();
    }
    
	// 결제 삭제 이벤트
    @Transactional
    public void deletePayment(Integer paymentIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
    	paymentRepository.deleteById(paymentIdx);
    }
    
    // ------------------------ 정산 -----------------------
    
    // 매장 정산 목록 리스트 조회
    public List<JeongsanListVO> findJeongsanList() {
        return adminMapper.findJeongsanList();
    }
    
    // 정산 상세 정보 조회
    public JeongsanListVO findJeongsanDetail(Integer Jeongsan_idx) {
        return adminMapper.findJeongsanDetail(Jeongsan_idx);
    }
    
    // 정산 상태 업데이트
    @Transactional
    public void updateJeongsanStatus(Integer jeongsanIdx, JeongsanUpdateVO JeongsanUpdateVO) {
        Jeongsan jeongsan = jeongsanRepository.findById(jeongsanIdx).orElseThrow();
        
        String newStatus = JeongsanUpdateVO.getCalculate_result();
        
        jeongsan.setCalculateResult(newStatus);
        jeongsan.setProcessedAt(LocalDateTime.now()); // 처리 시각 기록
        
		// jeongsan_status가 정상인지 확인
		if ("ctrt_01".equals(newStatus) || "ctrt_02".equals(newStatus)) {
			// 승인, 보류일 경우, 탈퇴 사유를 null
			jeongsan.setRejectionReason(null);
		} else {
			// 거부일 경우 거부 사유를 저장
			jeongsan.setRejectionReason(JeongsanUpdateVO.getRejection_reason());
		}
		
    }
    
	// 정산 삭제 이벤트
    @Transactional
    public void deleteJeongsan(Integer jeongsanIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
    	jeongsanRepository.deleteById(jeongsanIdx);
    }
    
    // -------------------------- 구독 ------------------------
    
    // 매장 구독 목록 리스트
    public List<SubscribeListVO> findSubscribeList() {
    	return adminMapper.findSubscribeList();
	}
    
	// 구독 삭제 이벤트
    @Transactional
    public void deleteSubscribe(Integer subscribeIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
    	subscribeRepository.deleteById(subscribeIdx);
    }
    
    // ---------------------- 공통코드 ----------------------
    
    // 공통 코드 목록 리스트
    public List<CommonCodeVO>findCommonCodeList() {
    	return adminMapper.findCommonCodeList();
    }
    
    // 공통코드 상세 정보 조회
    public CommonCodeVO findCommoncodeDetail(Integer common_idx) {
        return adminMapper.findCommoncodeDetail(common_idx);
    }
    
    // 공통 코드 업데이트
    @Transactional
    public void updateCommonCode(Integer common_idx, CommonCodeVO CommonCodeVO) {
    	adminMapper.updateCommonCode(common_idx, CommonCodeVO);
    }
    
    // 공통 코드 삭제
    public Integer deleteCommonCodeById(@Param("common_idx") Integer common_idx) {
    	return adminMapper.deleteCommonCodeById(common_idx);
    }
    
    // 그룹 목록 조회 메소드 (새로 추가)
    public List<CommonCodeGroupVO> findAllGroups() {
        return adminMapper.findAllGroups();
    }
    
    // 공통코드 그룹 추가
    public void saveCommonCodeGroup(CommonCodeGroupVO CommonCodeGroupVO) {

    	adminMapper.saveCommonCodeGroup(CommonCodeGroupVO);
    }
    
    // 공통 코드 추가
    public void saveCommonCode(List<CommonCodeVO> CommonCodeVO) {
    	
    	adminMapper.saveCommonCode(CommonCodeVO);
	}
    
    // ----------------------- 관리자 ---------------------
    
    // 관리자 회원 목록 조회
    public List<MemberListVO> adminMemberFindAll() {
        
        List<Member> memberList = memberRepository.findByMemberType("mtype_01");

        System.out.println("memberList : " + memberList);

        // Entity -> VO 변환 로직
        return memberList.stream()
                .map(entity -> new MemberListVO(entity, entity.getMemberIdx()))
                .collect(Collectors.toList());
    }
    
    // ------------------- 유저 로그인 로그 ---------------------
    
    // 유저 로그인 로그 목록 조회
    public List<UserLogVO> findUserLogList() {
    	
		return adminMapper.findUserLogList();
	}
    
	// 로그 삭제 이벤트
    @Transactional
    public void deleteUserLog(Integer LogIdx) {
        // jpa deleteById 활용 DB에서 데이터 삭제
    	userLogRepository.deleteById(LogIdx);
    }

}
