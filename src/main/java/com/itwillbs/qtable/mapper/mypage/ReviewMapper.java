package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
	
	List<Map<String, Object>> selectReviewsByMember(@Param("member_idx") String member_idx);

	void deleteReview(@Param("review_idx") int review_idx, @Param("member_idx") String member_idx);
	
	// 리뷰 1건 조회 (작성자 확인용)
    Map<String, Object> selectReviewById(String review_idx);

    // 리뷰 내용 수정
    void updateReviewContent(@Param("review_idx") String review_idx,
                             @Param("content") String content,
                             @Param("member_idx") String member_idx);

}
