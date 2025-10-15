package com.itwillbs.qtable.mapper.search;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface SearchKeywordListMapper {
	List<CommonCodeVO> selectSeatCntPriceRange (@Param("group_code") String group_code);
	List<Map<String,String>> getRegionLargeCategory();
	List<Map<String,Object>> getSubLocation();
}
