package com.itwillbs.qtable.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.qtable.entity.Member;
//import com.itwillbs.qtable.repository.MemberRepository;

@Controller
@RequestMapping("/test")
public class TestController {

    // 파일업로드 경로 설정 (application.properties에서 주입)
    @Value("${uploadPath}")
    private String uploadPath;

    // test1 페이지
    @GetMapping("/test1")
    public String test1() {
        return "test/test1";
    }

    // test1-1 페이지
    @GetMapping("/test1-1")
    public String test1_1() {
        return "test/test1-1";
    }

    // test2 페이지
    @GetMapping("/test2")
    public String test2() {
        return "test/test2";
    }

    // test3 페이지 - 이미지 업로드 테스트
    @GetMapping("/test3")
    public String test3() {
        return "test/test3";
    }

    // test4 페이지 - 업로드된 이미지 목록 조회
    @GetMapping("/test4")
    public String test4(Model model) {
        // 프로젝트 루트 경로 + uploadPath (upload 폴더)
        String projectPath = System.getProperty("user.dir");
        File uploadDir = new File(projectPath, uploadPath);

        // upload 폴더가 존재하면 이미지 파일 목록 조회
        if (uploadDir.exists()) {
            // 이미지 확장자 필터링 (jpg, jpeg, png, gif, bmp, webp)
            File[] files = uploadDir.listFiles((dir, name) ->
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$"));

            if (files != null) {
                // File 객체를 파일명 문자열 배열로 변환 (Thymeleaf 접근 제한 회피)
                String[] fileNames = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    fileNames[i] = files[i].getName();
                }
                model.addAttribute("uploadedFiles", fileNames);
            }
        }

        return "test/test4";
    }

    // 이미지 업로드 처리
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {
                // upload 폴더 경로 생성
                String projectPath = System.getProperty("user.dir");
                File uploadDir = new File(projectPath, uploadPath);

                // 폴더가 없으면 생성
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // UUID를 이용한 고유 파일명 생성 (중복 방지)
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir.getAbsolutePath(), fileName);

                // filePath경로에 업로드한 파일 복사
                Files.write(filePath, file.getBytes());

                // 업로드된 이미지 경로를 모델에 추가 (/upload/파일명)
                model.addAttribute("uploadedImage", "/upload/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "test/test3";
    }

//    @PostMapping("testInsert")
//    public String testInsert(@RequestParam("name") String name, @RequestParam("pw") String password) {
//        Member member = new Member();
//        member.setName(name);
//        member.setPassword(password);
//
//        memberRepository.save(member);
//
//        return "redirect:test2";
//    }
}
