package com.sancheck.backend.domain.user.dto.request;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class UserRequestDto {

  private String district; // 거주 자치구
  private String pregnancyStatus; // 임신 여부
  private Integer userAge; // 만 나이
  private Integer childCount; // 자녀 수

  // 선택 필드
  private LocalDate dueDate; // 출산 예정일
  private Integer infantMonths; // 영유아 개월 수
  private Boolean isMultibirth; // 다태아 여부
  private Boolean isForeigner; // 외국인 여부
  private Integer residenceMonths; // 거주 개월 수
  private Integer incomeLevel; // 소득 구간
  private Boolean isHomeless; // 무주택 여부
}