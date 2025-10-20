package com.itwillbs.qtable.config;

public class MaskingUtils {

	// 이름 마스킹 처리
    public static String maskName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        return name.substring(0, 1) + "*".repeat(name.length() - 1); // 마스킹 된 이름 -> 성을 제외한 이름을 *로
    }

    // 이메일 마스킹 처리
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];
        String maskedLocalPart;

        if (localPart.length() <= 3) {
        	// 아이디가 3글자 이하면 첫 글자만 빼고 마스킹
            maskedLocalPart = localPart.substring(0, 1) + "*".repeat(localPart.length() - 1);
        } else {
        	// 아이디가 4글자 이상이면 앞 3글자만 보여주고 마스킹
            maskedLocalPart = localPart.substring(0, 3) + "*".repeat(localPart.length() - 3);
        }
        return maskedLocalPart + "@" + domainPart;
    }
    
}