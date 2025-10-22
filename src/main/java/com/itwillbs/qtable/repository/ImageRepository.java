package com.itwillbs.qtable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.qtable.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	// 리스트 안의 매장 대표이미지 한번에 조회(승용)
    List<Image> findByTargetTypeAndTargetIdxIn(String targetType, List<Integer> storeIds);
    
    // 매장 대표 이미지 조회(영재)
 	Image findByTargetTypeAndTargetIdx(String targetType, Integer targetIdx);

}