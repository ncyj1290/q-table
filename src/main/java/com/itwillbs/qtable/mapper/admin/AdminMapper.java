package com.itwillbs.qtable.mapper.admin;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;

@Mapper
public interface AdminMapper {
	
	// 매장 회원 목록 리스트 조회
	List<StoreListVO> findStoreMembers();
	
	// 매장 입점 신청 목록 리스트 조회
	List<StoreListVO> findEntryStores();
	
	MemberDetailVO findMemberDetail(Integer member_idx);
	
}
