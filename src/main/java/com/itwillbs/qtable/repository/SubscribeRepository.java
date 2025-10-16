package com.itwillbs.qtable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Subscribe;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

}
