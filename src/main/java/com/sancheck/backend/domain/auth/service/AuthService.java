package com.sancheck.backend.domain.auth.service;

import com.sancheck.backend.domain.auth.client.KakaoApiClient;
import com.sancheck.backend.domain.auth.dto.response.KakaoAuthResponse;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import com.sancheck.backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoAuthResponse kakaoLogin(String authCode) {
        // 1. 인가 코드로 카카오 액세스 토큰 요청 (외부 통신 분리)
        String kakaoAccessToken = kakaoApiClient.getKakaoAccessToken(authCode);

        // 2. 토큰으로 카카오 사용자 정보 요청 (외부 통신 분리)
        Map<String, Object> userInfo = kakaoApiClient.getKakaoUserInfo(kakaoAccessToken);
        Long kakaoId = (Long) userInfo.get("id");

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = kakaoAccount != null && kakaoAccount.get("email") != null 
                        ? (String) kakaoAccount.get("email") : "kakao-" + kakaoId + "@kakao.com";
        
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
        String nickname = profile != null && profile.get("nickname") != null 
                        ? (String) profile.get("nickname") : "KakaoUser" + kakaoId;

        // 3. DB 연동: 유저 확인 및 신규 가입 처리
        boolean isNewUser = false;
        User user = userRepository.findByKakaoId(kakaoId).orElse(null);
        if (user == null) {
            user = User.builder()
                    .kakaoId(kakaoId)
                    .email(email)
                    .nickname(nickname)
                    .build();
            userRepository.save(user);
            isNewUser = true;
        }

        // 4. 서비스 토큰 발급 (DB에 저장된 user.getId() 활용)
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));

        return new KakaoAuthResponse(accessToken, isNewUser);
    }
}
