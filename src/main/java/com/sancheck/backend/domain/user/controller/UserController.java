package com.sancheck.backend.domain.user.controller;

import com.sancheck.backend.domain.user.dto.request.OnboardingRequestDto;
import com.sancheck.backend.domain.user.dto.request.UserRequestDto;
import com.sancheck.backend.domain.user.dto.response.UserResponseDto;
import com.sancheck.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserController {
  private final UserService userService;

  // 회원 정보 조회
  @GetMapping("")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserResponseDto> getUserInfo(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok(userService.getUserInfo(userId));
  }

  // 조건 정보 수정
  @PutMapping("")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> updateUserProfile(
      Authentication authentication,
      @RequestBody UserRequestDto request) {
    Long userId = Long.parseLong(authentication.getName());
    userService.updateUserProfile(userId, request);
    return ResponseEntity.ok().build();
  }

  // 온보딩 초기 정보 저장
  @PostMapping("/onboarding")
  public ResponseEntity<Void> saveOnboarding(
      Authentication authentication,
      @RequestBody OnboardingRequestDto request) {
    if (authentication != null) {
      Long userId = Long.parseLong(authentication.getName());
      userService.saveOnboarding(userId, request);
    }
    return ResponseEntity.ok().build();
  }

  // 회원 탈퇴
  @DeleteMapping("")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteUser(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    userService.deleteUser(userId);
    return ResponseEntity.ok().build();
  }

  // 프로필 이미지 등록 및 수정
  @PostMapping(value = "/image", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<String> updateProfileImage(
      Authentication authentication,
      @RequestPart("file") org.springframework.web.multipart.MultipartFile file) {
    Long userId = Long.parseLong(authentication.getName());
    String imageUrl = userService.updateProfileImage(userId, file);
    return ResponseEntity.ok(imageUrl);
  }
}