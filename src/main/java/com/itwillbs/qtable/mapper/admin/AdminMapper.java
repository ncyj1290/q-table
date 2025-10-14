package com.itwillbs.qtable.mapper.admin;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.admin.JeongsanListVO;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.PaymentListVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;

@Mapper
public interface AdminMapper {
	
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
	
}
