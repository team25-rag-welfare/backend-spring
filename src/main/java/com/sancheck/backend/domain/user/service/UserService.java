package com.sancheck.backend.domain.user.service;

import com.sancheck.backend.domain.user.dto.response.UserResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sancheck.backend.domain.user.dto.request.UserRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // 회원 정보 조회
  public UserResponseDto getUserInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    return new UserResponseDto(user);
  }

  // 조건 정보 수정
  public void updateUserProfile(Long userId, UserRequestDto request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    user.updateProfile(request);
    userRepository.save(user);
  }
}