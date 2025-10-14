package com.itwillbs.qtable.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "kakaopay")
public class KakaoPayProperties {

	private String secretkey;
	private String cid;
	
}
