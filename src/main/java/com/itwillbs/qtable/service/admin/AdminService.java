package com.itwillbs.qtable.service.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.config.MaskingUtils;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Jeongsan;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.mapper.admin.AdminMapper;
import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;
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
	private StoreDetailMapper storeMapper;

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

		List<MemberListVO> memberVos = new ArrayList<>();

		// 가져온 엔티티 리스트를 순회하며 VO로 변환
		for (Member entity : memberList) {
			MemberListVO vo = new MemberListVO(entity, entity.getMemberIdx());
			// 마스킹 전용 메서드 (MemberListVO)
			vo.applyMasking();

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

		// MemberDetailVO에 마스킹 함수
		if (vo != null) {
			vo.applyMasking();
		}
		
		return vo;
	}

	// member_idx로 매장 상세 정보를 조회
	public StoreDetailVO findByMemberIdx(Integer member_idx) {

		return storeRepository.findByMemberIdx(member_idx).map(StoreDetailVO::new).orElse(null);
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

		List<StoreListVO> storeMemberList = adminMapper.findStoreMembers();

		// 리스트를 순회하며 마스킹 조건을 확인하고 적용
		for (StoreListVO vo : storeMemberList) {
			// 마스킹 전용 메서드 (StoreListVO)
			vo.applyMasking();
		}
		return storeMemberList;
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
			store.setRejectionReason(null);
			;
		} else {
			// 반려일 경우 반려 사유를 저장
			store.setRejectionReason(StoreUpdateVO.getRejection_reason());
		}

	}

	// 매장 삭제
	@Transactional
	public void deleteStoreMember(Integer member_idx) {

		// store_idx를 찾기
		Integer store_idx = storeRepository.findByMemberIdx(member_idx).map(Store::getStoreIdx).orElse(null);

		// store_idx가 존재할 경우 하위 데이터를 삭제
		if (store_idx != null) {
			storedata.deleteHolidayByStoreIdx(store_idx);
			storedata.deleteCategoryByStoreIdx(store_idx);
			storedata.deleteAmenityByStoreIdx(store_idx);
			storedata.deleteAtmosphereByStoreIdx(store_idx);
			storedata.deleteIngredientByStoreIdx(store_idx);
			storedata.deleteMenuByStoreIdx(store_idx);

			// 매장과 관련된 이미지 타입들을 배열로 정의
			String[] imageTypesToDelete = { "imguse_01", "imguse_02", "imguse_03", "imguse_04", "imguse_05" };

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

		List<PaymentListVO> paymentList = adminMapper.findPaymentListMembers();

		// 리스트를 순회하며 마스킹 조건을 확인하고 적용
		for (PaymentListVO vo : paymentList) {
			// 마스킹 전용 메서드 (PaymentListVO)
			vo.applyMasking();
		}
		return paymentList;
	}

	// 매장 결제 목록 리스트 조회
	public List<PaymentListVO> findPaymentListStores() {

		List<PaymentListVO> paymentStoreList = adminMapper.findPaymentListStores();

		// 리스트를 순회하며 마스킹 조건을 확인하고 적용
		for (PaymentListVO vo : paymentStoreList) {
			// 마스킹 전용 메서드 (PaymentListVO)
			vo.applyMasking();
		}
		return paymentStoreList;
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
		List<JeongsanListVO> JeongsanList = adminMapper.findJeongsanList();

		// 리스트를 순회하며 마스킹 조건을 확인하고 적용
		for (JeongsanListVO vo : JeongsanList) {
			// 마스킹 전용 메서드 (JeongsanListVO)
			vo.applyMasking();
		}
		return JeongsanList;
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
	public List<CommonCodeVO> findCommonCodeList() {
		return adminMapper.findCommonCodeList();
	}

	// 공통코드 상세 정보 조회
	public CommonCodeVO findCommoncodeDetail(Integer common_idx) {
		return adminMapper.findCommoncodeDetail(common_idx);
	}

	// 공통 코드 업데이트
	@Transactional
	public void updateCommonCode(Integer common_idx, CommonCodeVO CommonCodeVO) {
		
		int currentAdminIdx = getCurrentAdminIdx();
		LocalDateTime now = LocalDateTime.now();
		
		adminMapper.updateCommonCode(common_idx, CommonCodeVO, currentAdminIdx, now);
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
		
		int currentAdminIdx = getCurrentAdminIdx();
		
		CommonCodeGroupVO.setCreater_idx(currentAdminIdx);
		
		
		System.out.println("@#%@#%@#%#@%#@%#@%@#%#@%");
		System.out.println("CommonCodeGroupVO : " + CommonCodeGroupVO);

		adminMapper.saveCommonCodeGroup(CommonCodeGroupVO);
	}

	// 공통 코드 추가
	public void saveCommonCode(List<CommonCodeVO> CommonCodeVO) {
		
		int currentAdminIdx = getCurrentAdminIdx();
		
		for (CommonCodeVO vo : CommonCodeVO) {
            // 각 VO에 생성자 ID 설정
            vo.setCreater_idx(currentAdminIdx);
        }

		adminMapper.saveCommonCode(CommonCodeVO);
	}

	// ----------------------- 관리자 ---------------------

	// 관리자 회원 목록 조회
	public List<MemberListVO> adminMemberFindAll() {

		// 조회할 회원 타입 지정
		String type1 = "mtype_01"; // 관리자

		// member_type이 type1 일치하는 member 리스트 + member_idx 내림차순
		List<Member> memberList = memberRepository.findByMemberType(type1);

		List<MemberListVO> memberVos = new ArrayList<>();

		// 가져온 엔티티 리스트를 순회하며 VO로 변환
		for (Member entity : memberList) {
			MemberListVO vo = new MemberListVO(entity, entity.getMemberIdx());
			// 마스킹 전용 메서드 (MemberListVO)
			vo.applyMasking();

			memberVos.add(vo);
		}

		return memberVos;
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
	
	// 매장 이미지 가져오기
	public Map<String, Object> getStoreInfoByMemberIdx(Integer member_idx) {
		// member_idx로 store_idx를 조회
		Integer storeIdx = adminMapper.getStoreIdxByMemberIdx(member_idx);
		
		// 매장 존재 여부 체크
		if (storeIdx == null) {
			return new HashMap<>();
		}

		// 매장의 기본 정보 조회
		Map<String, Object> result = storeMapper.getStoreBasicInfo(storeIdx);

		// 매장 이미지 조회 (null 방어)
		List<String> profileImage = storeMapper.getStoreProfileImage(storeIdx);
		List<String> images = storeMapper.getStoreImage(storeIdx);

		// 매장 프로필이미지 + 매장 사진들 리스트 
		List<String> storeImages = Stream.concat(
			profileImage != null ? profileImage.stream() : Stream.empty(),
		    images != null ? images.stream() : Stream.empty()
		)
		.toList();
		
		result.put("store_images", storeImages.isEmpty() ? List.of("/img/logo.png") : storeImages);

		return result;
	}
	
	
	
	
	
	// --- 현재 관리자 ID를 가져오는 메서드
    private int getCurrentAdminIdx() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        System.out.println("authentication : " + authentication);
        
     // 인증 정보가 있고, Principal이 QtableUserDetails 타입이면 idx 반환
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof QtableUserDetails) {
            return ((QtableUserDetails) authentication.getPrincipal()).getMemberIdx();
        }
        return 0; // 또는 예외 처리
    }

}
