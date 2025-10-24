package com.itwillbs.qtable.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
	
    // member_idx 컬럼으로 Store 엔티티를 찾는 메소드
    Optional<Store> findByMemberIdx(Integer member_idx);
    
    @Query(value = """
    	    SELECT s.*, COALESCE(AVG(r.score), 0) AS avg_score
    	    FROM store s
    	    LEFT JOIN review r ON s.store_idx = r.store_idx
    	    GROUP BY s.store_idx
    	    ORDER BY avg_score DESC
    	    """, nativeQuery = true)
        List<Store> findAllOrderByAvgRatingDescNative();
    
}
