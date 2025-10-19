package com.itwillbs.qtable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.qtable.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByTargetTypeAndTargetIdxIn(String targetType, List<Integer> storeIds);
}