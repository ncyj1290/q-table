package com.itwillbs.qtable.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;



@Service
public class FileUploadService {
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	
	/* 파일 저장 및 저장 경로 반환 함수 */
	public String saveFileAndGetPath(MultipartFile uploadFile) throws Exception{
		
		/* 파일 X -> 않으면 예외 발생 */
		if(uploadFile == null || uploadFile.isEmpty()) throw new Exception("파일이 비어있습니다.");
		
        // 날짜별 폴더 구조 생성 (예: 2025/10/13)
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		
		/* 경로 세팅 -> 없으면 폴더 생성 */
		String projectPath = System.getProperty("user.dir");
		File dir = new File(projectPath, uploadPath + "/" + datePath);
		if(!dir.exists()) dir.mkdirs();
		
		/* 업로드 파일 원본 경로 */
		String originalPath = uploadFile.getOriginalFilename();
		String ext = "";
		
		System.out.println("Original File Path Check: " + originalPath);
		
		if (originalPath != null) {
			
			/* 확장자 추출을 위해 마지막 (.) 기반으로 분리 */
            int dot = originalPath.lastIndexOf('.');
            if (dot != -1) ext = originalPath.substring(dot);
            originalPath = originalPath.substring(0, dot);
            
            System.out.println("Dot, Ext, Original Path: " + dot + ", " + ext + ", " + originalPath);
        }
		
		/* UUID 추가 파일명 생성 및 저장 */
		/* 저장 형식은 원본 이름_UUID.확장자 로 일단 해놓긴함 */
		String savePath = originalPath + "_" + UUID.randomUUID().toString() + ext;
		uploadFile.transferTo(new File(dir, savePath));
		
		return "/upload/" + datePath + "/" + savePath;
	}
	
	
	/* DB에 저장된 경로대로 파일 삭제하는 함수 */
	public boolean deleteFileByPath(String path) throws Exception {
		
		/* Null -> Return False */
		if(path == null || path.isBlank()) return false;
		
		path = path.replace('\\', '/');
		
		String prefix = "/" + uploadPath + "/";
		
		/* Prefix, '/' 제거 */
		if(path.startsWith(prefix)) path = path.substring(prefix.length());
		if(path.startsWith("/")) path = path.substring(1);
		
		/* 상대 경로 해서 project/upload/ 까지 잡아주는거라고 함 */
		Path base = Paths.get(System.getProperty("user.dir"))
				.resolve(uploadPath)
				.toAbsolutePath()
				.normalize();
		
		/* 지울 놈 */
		Path target = base.resolve(path).normalize();
		
		/* 경로 벗어나면 false */
		if(!target.startsWith(base)) return false;
		
		return Files.deleteIfExists(target);
	}
	
	
	
	
	

}
