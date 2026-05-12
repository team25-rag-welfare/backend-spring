package com.sancheck.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import java.time.LocalDateTime;
import com.sancheck.backend.global.common.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId; // 카카오 고유 ID

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String userName; // user_name

    @Column
    private Integer userAge; // 만 나이

    @Column(nullable = false)
    private String pregnancyStatus; // 임신 여부

    @Column(nullable = false)
    private String district; // 거주 자치구

    @Column(nullable = false)
    private Integer childCount; // 자녀 수

    @Column(nullable = false)
    private Boolean isDeleted = false; // 탈퇴 여부

    @Column(nullable = false)
    private LocalDateTime termsAgreedAt; // 약관 동의 시각

    // 선택 필드
    @Column
    private Boolean isMultibirth; // 다태아 여부

    @Column
    private Boolean isForeigner; // 외국인 여부

    @Column
    private LocalDateTime deletedAt; // 탈퇴 일시

    @Column
    private Integer residenceMonths; // 거주 개월 수

    @Column
    private Integer pregnancyWeeks; // 임신 주차

    @Column
    private Integer infantMonths; // 영유아 개월 수

    @Column
    private Integer incomeLevel; // 소득 구간

    @Column
    private Boolean isHomeless; // 무주택 여부

    @Builder
    public User(Long kakaoId, String email, String nickname,
        String userName, Integer userAge, String pregnancyStatus,
        String district, Integer childCount, Boolean isDeleted,
        LocalDateTime termsAgreedAt, LocalDateTime createdAt,
        Boolean isMultibirth, Boolean isForeigner, LocalDateTime deletedAt,
        Integer residenceMonths, Integer pregnancyWeeks, Integer infantMonths,
        Integer incomeLevel, Boolean isHomeless) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
        this.userName = userName;
        this.userAge = userAge;
        this.pregnancyStatus = pregnancyStatus;
        this.district = district;
        this.childCount = childCount;
        this.isDeleted = isDeleted;
        this.termsAgreedAt = termsAgreedAt;
        this.isMultibirth = isMultibirth;
        this.isForeigner = isForeigner;
        this.deletedAt = deletedAt;
        this.residenceMonths = residenceMonths;
        this.pregnancyWeeks = pregnancyWeeks;
        this.infantMonths = infantMonths;
        this.incomeLevel = incomeLevel;
        this.isHomeless = isHomeless;
    }
}
