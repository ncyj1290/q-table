package com.itwillbs.qtable.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${uploadPath}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 절대 경로로 변환
		String projectPath = System.getProperty("user.dir");
		File uploadDir = new File(projectPath, uploadPath);
		String absolutePath = uploadDir.getAbsolutePath();

		// /upload/** 요청을 uploadPath 폴더로 매핑
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///" + absolutePath + "/");
	}
}
