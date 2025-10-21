package com.itwillbs.qtable.service.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
	@Autowired
    private ReviewMapper reviewMapper;

    public List<Map<String, Object>> getMyReviews(String memberIdx) {
        return reviewMapper.selectReviewsByMember(memberIdx);
    }

}
