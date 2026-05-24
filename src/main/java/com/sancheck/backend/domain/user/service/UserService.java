package com.sancheck.backend.domain.user.service;

import com.sancheck.backend.domain.user.dto.response.UserResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import com.sancheck.backend.domain.memory.repository.MemoryRepository;
import com.sancheck.backend.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sancheck.backend.domain.user.dto.request.UserRequestDto;
import com.sancheck.backend.domain.user.dto.request.OnboardingRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MemoryRepository memoryRepository;
  private final ChatMessageRepository chatMessageRepository;

  // 회원 정보 조회
  public UserResponseDto getUserInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    return new UserResponseDto(user);
  }

  // 조건 정보 수정
  @Transactional
  public void updateUserProfile(Long userId, UserRequestDto request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    user.updateProfile(request);
    userRepository.save(user);
  }

  // 온보딩 초기 정보 저장
  @Transactional
  public void saveOnboarding(Long userId, OnboardingRequestDto request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    user.saveOnboarding(request);
    userRepository.save(user);
  }

  // 회원 탈퇴
  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    
    // 1. 회원 상태 변경 (Soft Delete)
    user.withdraw();
    userRepository.save(user);

    // 2. 메모리 벌크 삭제 (Soft Delete)
    memoryRepository.softDeleteAllByUser(user);

    // 3. 채팅 메시지 삭제 (Hard Delete)
    chatMessageRepository.deleteByUserId(userId);
  }
}