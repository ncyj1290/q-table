package com.itwillbs.qtable.mapper.search;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface SearchKeywordListMapper {
	List<CommonCodeVO> selectSeatCntPriceRange (@Param("group_code") String group_code);
}
