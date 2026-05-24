package com.sancheck.backend.domain.auth.controller;

import com.sancheck.backend.domain.auth.dto.request.KakaoAuthRequest;
import com.sancheck.backend.domain.auth.dto.response.KakaoAuthResponse;
import com.sancheck.backend.domain.auth.service.AuthService;
import com.sancheck.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<KakaoAuthResponse> kakaoLogin(@RequestBody KakaoAuthRequest request) {
        KakaoAuthResponse response = authService.kakaoLogin(request.getAuth_code());
        return ResponseEntity.ok(response);
    }

    // 약관 동의 API - 로그인한 회원만 호출 가능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/terms")
    public ResponseEntity<Void> agreeTerms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.agreeTerms(userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    // 로그아웃 API (Stateless 방식)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Stateless 방식이므로 백엔드에서는 별도의 토큰 삭제 로직 없이 성공 응답만 반환
        // 실제 토큰 삭제는 프론트엔드(클라이언트)에서 수행합니다.
        return ResponseEntity.ok().build();
    }
}
