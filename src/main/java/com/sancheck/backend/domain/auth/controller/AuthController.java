package com.sancheck.backend.domain.auth.controller;

import com.sancheck.backend.domain.auth.dto.request.KakaoAuthRequestDto;
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
    public ResponseEntity<KakaoAuthResponse> kakaoLogin(@RequestBody KakaoAuthRequestDto request) {
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
}
