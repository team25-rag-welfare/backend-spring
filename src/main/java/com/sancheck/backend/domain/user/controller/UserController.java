package com.sancheck.backend.domain.user.controller;

import com.sancheck.backend.domain.user.dto.response.UserResponseDto;
import com.sancheck.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class UserController {
  private final UserService userService;

  // 회원 정보 조회
  @GetMapping("")
  public ResponseEntity<UserResponseDto> getUserInfo(@RequestHeader("userId") Long userId) {
    return ResponseEntity.ok(userService.getUserInfo(userId));
  }
}
