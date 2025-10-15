package com.itwillbs.qtable.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final StoreRepository storeRepository;
	
	  public List<Store> getAllStores() {
	        return storeRepository.findAll();
	    }
	
}
