package com.sancheck.backend.domain.user.dto.request;

import lombok.Getter;

@Getter
public class OnboardingRequestDto {

  private String district; // 거주 자치구 (필수)
  private String pregnancyStatus; // 임신 여부 (필수)
  private Integer userAge; // 만 나이 (필수)
  private Integer childCount; // 자녀 수 (필수)
}