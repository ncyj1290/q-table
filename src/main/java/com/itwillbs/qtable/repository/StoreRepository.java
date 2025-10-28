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
		    SELECT s.store_idx,
		           s.store_name,
		           s.sido,
		           s.deposit,
		           s.full_address,
		           s.open_time,
		           s.close_time,
		           COALESCE(AVG(r.score),0) AS avg_score,
		           COUNT(r.review_idx) AS review_count
		    FROM store s
		    LEFT JOIN review r ON s.store_idx = r.store_idx
		    INNER JOIN subscribe sub ON s.member_idx = sub.member_idx
		    WHERE s.store_status = 'srst_01' AND s.is_accept = true
		    GROUP BY s.store_idx, s.store_name, s.sido, s.deposit, s.full_address, s.open_time, s.close_time
		    ORDER BY avg_score DESC , review_count DESC
		    
		    """, nativeQuery = true)
		List<Object[]> findStoresWithAvgScoreNative();

}
