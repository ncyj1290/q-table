package com.itwillbs.qtable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.UserLog;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Integer>{
	
}
