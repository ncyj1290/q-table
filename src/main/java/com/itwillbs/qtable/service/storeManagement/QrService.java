package com.itwillbs.qtable.service.storeManagement;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class QrService {

	@Value("${app.public-base-url}")
	private String publicBaseUrl;
	
	public String buildUrl(int storeIdx) {
	    return UriComponentsBuilder
	            .fromUriString(publicBaseUrl)         
	            .pathSegment("on_site_payment")       
	            .queryParam("store_idx", storeIdx)
	            .build()                               
	            .encode()                               
	            .toUriString();
	}

	
	
	
	
	
	
	
}
