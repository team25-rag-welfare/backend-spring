package com.sancheck.backend.domain.auth.service;

import com.sancheck.backend.domain.auth.client.KakaoApiClient;
import com.sancheck.backend.domain.auth.dto.response.KakaoAuthResponse;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import com.sancheck.backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

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
                    // nullable=false 필드에 임시 기본값 설정 (온보딩 단계에서 실제 값으로 갱신됨)
                    .userName(nickname)       // 이름은 일단 카카오 닉네임으로 초기화
                    .pregnancyStatus("NONE")  // 임신 여부: 지정 안 함
                    .district("NONE")         // 거주 자치구: 지정 안 함
                    .childCount(0)            // 자녀 수: 0명
                    .isDeleted(false)
                    .termsAgreedAt(LocalDateTime.now()) // 약관 동의: 가입 시각으로 초기화
                    .build();
            userRepository.save(user);
            isNewUser = true;
        }

        // 4. 서비스 토큰 발급 (DB에 저장된 user.getId() 활용)
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));

        return new KakaoAuthResponse(accessToken, isNewUser);
    }

    @PreAuthorize("isAuthenticated()")
    public void agreeTerms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.agreeTerms(LocalDateTime.now());
        userRepository.save(user);
    }
}
