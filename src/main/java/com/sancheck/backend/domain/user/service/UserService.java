package com.sancheck.backend.domain.user.service;

import com.sancheck.backend.domain.user.dto.response.UserResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import com.sancheck.backend.domain.memory.repository.MemoryRepository;
import com.sancheck.backend.domain.chat.repository.ChatMessageRepository;
import com.sancheck.backend.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.sancheck.backend.domain.user.dto.request.UserRequestDto;
import com.sancheck.backend.domain.user.dto.request.OnboardingRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MemoryRepository memoryRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final S3Service s3Service;

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

  // 프로필 이미지 수정
  @Transactional
  public String updateProfileImage(Long userId, MultipartFile file) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

    // 기존 프로필 이미지가 존재하면 S3에서 삭제
    String oldImageUrl = user.getProfileImageUrl();
    if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
      try {
        s3Service.deleteFile(oldImageUrl);
      } catch (Exception e) {
        // 기존 파일 삭제 실패 시 로그만 출력하고 계속 진행 (비정상 URL 등 대비)
        System.err.println("기존 프로필 이미지 삭제 실패: " + e.getMessage());
      }
    }

    // 신규 이미지 S3 업로드
    String newImageUrl = s3Service.uploadFile(file, "profiles");
    user.updateProfileImageUrl(newImageUrl);
    userRepository.save(user);

    return newImageUrl;
  }
}