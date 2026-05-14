package com.sancheck.backend.domain.auth.controller;

import com.sancheck.backend.domain.auth.dto.request.KakaoAuthRequestDto;
import com.sancheck.backend.domain.auth.dto.response.KakaoAuthResponseDto;
import com.sancheck.backend.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<KakaoAuthResponseDto> kakaoLogin(@RequestBody KakaoAuthRequestDto request) {
        KakaoAuthResponseDto response = authService.kakaoLogin(request.getAuth_code());
        return ResponseEntity.ok(response);
    }
}
