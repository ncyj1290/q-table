package com.itwillbs.qtable.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
	
    // member_idx 컬럼으로 Store 엔티티를 찾는 메소드
    Optional<Store> findByMemberIdx(Integer member_idx);

}
