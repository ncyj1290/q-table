package com.itwillbs.qtable.config;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.vo.member.KakaoMemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Service
public class QtableOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository repo;
    private final RestTemplate restTemplate = new RestTemplate(); // 배송지 API 호출용
    @Value("${kakao.shipping-address-uri}")
    private String kakaoShippingAddressUri;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        Member member = saveOrUpdate(registrationId, attributes, userRequest.getAccessToken().getTokenValue(), userNameAttributeName);

        return new QtableOAuth2User(member, oAuth2User);
    }

    private Member saveOrUpdate(String registrationId, Map<String, Object> attributes, String accessToken, String userNameAttributeName) {
    	 if ("kakao".equals(registrationId)) {
    	        String userId = registrationId + "_" + attributes.get(userNameAttributeName).toString();
    	        
    	        // Optional 활용
    	        Optional<Member> optionalMember = repo.findByMemberId(userId);
    	        if (optionalMember.isPresent()) {
    	            // 기존 회원이면 필요한 정보만 업데이트
    	            Member existingMember = optionalMember.get();
    	            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    	            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
    	            existingMember.setProfileImgUrl(profile != null ? (String) profile.get("profile_image_url") : null);
    	            existingMember.setEmail((String) kakaoAccount.get("email"));
    	            return repo.save(existingMember);
    	        }

    	        // 새 회원이면 기존 로직
    	        return repo.save(getKakaoMember(registrationId, attributes,accessToken,userNameAttributeName));
    	    }

    	    return null;
    }

    private Member getKakaoMember(String registrationId, Map<String, Object> attributes, String accessToken, String userNameAttributeName) {
        if (attributes == null) return null;

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount == null) return null;

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // 1. 기본 정보
        String userId = registrationId + "_" + attributes.get(userNameAttributeName);
        String email = (String) kakaoAccount.get("email");
        String name = kakaoAccount != null ? (String) kakaoAccount.get("name") : null;
        String gender = "male".equals(kakaoAccount.get("gender")) ? "gender_01" : "gender_02";
        String profileImage = profile != null ? (String) profile.get("profile_image_url") : null;
        String birthday = buildBirthday(kakaoAccount);

        // 2. 배송지 API 호출
        String baseAddress = null;
        String detailAddress = null;
        String zoneNo = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(kakaoShippingAddressUri, HttpMethod.GET, entity, Map.class);
            List<Map<String, Object>> shippingAddresses = (List<Map<String, Object>>) response.getBody().get("shipping_addresses");

            if (shippingAddresses != null && !shippingAddresses.isEmpty()) {
                Map<String, Object> firstAddress = shippingAddresses.get(0);
                baseAddress = (String) firstAddress.get("base_address");
                detailAddress = (String) firstAddress.get("detail_address");
                zoneNo = (String) firstAddress.get("zone_number");
            }
        } catch (Exception e) {
            log.warning("배송지 API 호출 실패: " + e.getMessage());
        }
        
        KakaoMemberVO vo = KakaoMemberVO.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .gender(gender)
                .profileImage(profileImage)
                .baseAddress(baseAddress)
                .detailAddress(detailAddress)
                .zoneNo(zoneNo)
                .birthday(birthday)
                .build();

        return convertToMemberEntityNoEncrypt(vo);
    }

    private String buildBirthday(Map<String, Object> kakaoAccount) {
        String birthYear = (String) kakaoAccount.get("birthyear");
        String birthday = (String) kakaoAccount.get("birthday"); // MMdd
        if (birthYear != null && birthday != null && birthday.length() == 4) {
            return birthYear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2, 4);
        }
        return null;
    }

    private Member convertToMemberEntityNoEncrypt(KakaoMemberVO vo) {
        LocalDate birthDate = vo.getBirthday() != null ? LocalDate.parse(vo.getBirthday()) : null;

        return Member.builder()
                .memberId(vo.getUserId())
                .memberName(vo.getName())
                .email(vo.getEmail())
                .gender(vo.getGender())
                .profileImgUrl(vo.getProfileImage())
                .address(vo.getBaseAddress())
                .addressDetail(vo.getDetailAddress())
                .postCode(vo.getZoneNo())
                .birth(birthDate)
                .memberPw(UUID.randomUUID().toString())
                .memberType("mtype_02")
                .mailAuthStatus(true)
                .build();
    }
}
