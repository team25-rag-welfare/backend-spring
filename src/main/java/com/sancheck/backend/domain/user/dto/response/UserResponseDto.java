package com.sancheck.backend.domain.user.dto.response;

import com.sancheck.backend.domain.user.entity.User;
import lombok.Getter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class UserResponseDto {
  private Long userId;
  private String userName;
  private Integer userAge;
  private String pregnancyStatus;
  private String district;
  private Integer childCount;
  private Boolean isMultibirth;
  private Boolean isForeigner;
  private Integer residenceMonths;
  private Integer pregnancyWeeks;
  private Integer infantMonths;
  private Integer incomeLevel;
  private Boolean isHomeless;
  private String profileImageUrl;


  public UserResponseDto(User user) {
    this.userId = user.getId();
    this.userName = user.getUserName();
    this.userAge = user.getUserAge();
    this.pregnancyStatus = user.getPregnancyStatus();
    this.district = user.getDistrict();
    this.childCount = user.getChildCount();
    this.isMultibirth = user.getIsMultibirth();
    this.isForeigner = user.getIsForeigner();
    this.residenceMonths = user.getResidenceMonths();
    this.infantMonths = user.getInfantMonths();
    this.incomeLevel = user.getIncomeLevel();
    this.isHomeless = user.getIsHomeless();
    this.profileImageUrl = user.getProfileImageUrl();
    if (user.getDueDate() != null) {
      long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), user.getDueDate());
      this.pregnancyWeeks = (int) ((280 - daysUntilDue) / 7);
    }
  }
}
