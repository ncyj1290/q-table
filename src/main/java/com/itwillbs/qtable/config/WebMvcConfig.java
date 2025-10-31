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

		// 정적 리소스 경로 명시적 설정 (WAR 배포 시 필요)
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");

		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/");

		registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/");
	}
}
