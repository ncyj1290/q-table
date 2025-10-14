package com.itwillbs.qtable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Jeongsan;

@Repository
public interface JeongsanRepository extends JpaRepository<Jeongsan, Integer> {

}
