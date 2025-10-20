package com.itwillbs.qtable.mapper.admin;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.admin.JeongsanListVO;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.PaymentListVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;
import com.itwillbs.qtable.vo.admin.SubscribeListVO;
import com.itwillbs.qtable.vo.admin.UserLogVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeGroupVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface AdminMapper {
	
	// 관리자 메인화면 일주일간 신규 회원
	int countNewMembers();
	
	// 관리자 메인화면 일주일간 신규 매장회원
	int countNewStoreMembers();
	
	// 관리자 메인화면 1일 신규 입점 신청
	int countNewStoreEntry();
	
	// 매장 회원 목록 리스트 조회
	List<StoreListVO> findStoreMembers();
	
	// 매장 입점 신청 목록 리스트 조회
	List<StoreListVO> findEntryStores();
	
	// 회원 / 매장 상세페이지 조회
	MemberDetailVO findMemberDetail(Integer member_idx);
	
	// 회원 결제 목록 리스트 조회
	List<PaymentListVO> findPaymentListMembers();
	
	// 매장 결제 목록 리스트 조회
	List<PaymentListVO> findPaymentListStores();
	
	// 정산 목록 리스트 조회
	List<JeongsanListVO> findJeongsanList();
	
	// 매장 정산 상세 조회
	JeongsanListVO findJeongsanDetail(Integer jeongsan_idx);
	
	// 매장 구독 목록 리스트 조회
	List<SubscribeListVO> findSubscribeList();
	
	// 공통 코드 목록 리스트 조회
	List<CommonCodeVO>findCommonCodeList();
	
	// 공통 코드 상세 조회
	CommonCodeVO findCommoncodeDetail(Integer common_idx);
	
	// 공통 코드 삭제
	Integer deleteCommonCodeById(@Param("common_idx") Integer common_idx);
	
	// 공통 코드 수정
    void updateCommonCode(@Param("common_idx") Integer common_idx, @Param("CommonCodeVO") CommonCodeVO CommonCodeVO);
    
    // 공통 코드 그룹 리스트
    List<CommonCodeGroupVO> findAllGroups();
    
    // 공통 코드 그룹 추가
    void saveCommonCodeGroup(CommonCodeGroupVO CommonCodeGroupVO);
    
    // 공통 코드 추가
    void saveCommonCode(@Param("list") List<CommonCodeVO> CommonCodeVO);
    
    // 회원 로그 목록 리스트 조회
    List<UserLogVO> findUserLogList();
	
}
