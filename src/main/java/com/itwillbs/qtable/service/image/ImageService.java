package com.itwillbs.qtable.service.image;


import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Image;
import com.itwillbs.qtable.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	  public List<Image> getMainImages(String targetType,  List<Integer> storeIds) {
	        return imageRepository.findByTargetTypeAndTargetIdxInAndIsMainImageTrue(targetType, storeIds);
	    }
}
