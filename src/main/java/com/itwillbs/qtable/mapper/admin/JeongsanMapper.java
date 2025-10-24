package com.itwillbs.qtable.mapper.admin;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.admin.JeongsanListVO;

@Mapper
public interface JeongsanMapper {
	
	// 정산 insert 로직
	int insertJeongsan(JeongsanListVO vo);
	
}
