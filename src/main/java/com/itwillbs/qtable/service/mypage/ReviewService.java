package com.itwillbs.qtable.service.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReviewMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
	@Autowired
    private ReviewMapper reviewMapper;
	
	//리뷰 조회
    public List<Map<String, Object>> selectReviewsByMember(String memberIdx) {
    	List<Map<String, Object>> reviews = reviewMapper.selectReviewsByMember(memberIdx);
        return reviewMapper.selectReviewsByMember(memberIdx);
    }
    
    //리뷰 삭제
    public void deleteReview(int reviewIdx, String memberIdx) {
        reviewMapper.deleteReview(reviewIdx, memberIdx);
    }

    @Transactional
    public void updateReviewContent(String reviewIdx, String content, String memberIdx) {

    	Map<String, Object> review = reviewMapper.selectReviewById(reviewIdx);
        // 작성자 검증
        String reviewMemberId = String.valueOf(review.get("member_idx")); // 컬럼명에 맞게 조정
        System.out.println("로그인 사용자 ID: " + memberIdx);
        System.out.println("리뷰 작성자 ID: " + reviewMemberId);

    	
    	if (review == null || review.isEmpty()) {
    	    throw new RuntimeException("존재하지 않는 리뷰입니다.");
    	}

    	if (!memberIdx.equals(reviewMemberId)) {
    	    throw new RuntimeException("수정 권한이 없습니다.");
    	}

        // 리뷰 내용 수정
        reviewMapper.updateReviewContent(reviewIdx, content, memberIdx);
    }


}
