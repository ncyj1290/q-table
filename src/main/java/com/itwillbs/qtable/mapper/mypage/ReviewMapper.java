package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
	
	List<Map<String, Object>> selectReviewsByMember(@Param("member_idx") String member_idx);

	void deleteReview(@Param("review_idx") int review_idx, @Param("member_idx") String member_idx);
}
