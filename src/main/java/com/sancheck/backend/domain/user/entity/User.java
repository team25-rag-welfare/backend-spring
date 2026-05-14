package com.sancheck.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // 유저 식별자 (PK)

    @Column(name = "social_id", nullable = false)
    private String socialId; // 카카오 고유 ID 등

    @Column(name = "social_type", nullable = false)
    private String socialType; // 예: "KAKAO", "NAVER"

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String userName; // 사용자 이름

    @Column(name = "user_age", nullable = false)
    private Integer userAge; // 만 나이

    @Column(name = "pregnancy_status", nullable = false)
    private String pregnancyStatus; // 생애 단계

    @Column(name = "district", nullable = false)
    private String district; // 거주 자치구

    @Column(name = "child_count", nullable = false)
    private Integer childCount = 0; // 자녀 수 (기본값 세팅)

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // 탈퇴 여부 (Soft Delete)

    @Column(name = "terms_agreed_at", nullable = false)
    private LocalDateTime termsAgreedAt; // 약관 동의 시각

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 가입 일시

    // ----------------------------------------------------
    // 💡 여기서부터는 ERD 상 'null 허용' 필드들입니다.
    // ----------------------------------------------------

    @Column(name = "is_multibirth")
    private Boolean isMultibirth;

    @Column(name = "is_foreigner")
    private Boolean isForeigner;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "residence_months")
    private Integer residenceMonths;

    @Column(name = "pregnancy_weeks")
    private Integer pregnancyWeeks;

    @Column(name = "infant_months")
    private Integer infantMonths;

    @Column(name = "income_level")
    private Integer incomeLevel;

    @Column(name = "is_homeless")
    private Boolean isHomeless;

    @Builder
    public User(String socialId, String socialType, String email, String userName, Integer userAge,
        String pregnancyStatus, String district, Integer childCount,
        LocalDateTime termsAgreedAt) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.email = email;
        this.userName = userName;
        this.userAge = userAge;
        this.pregnancyStatus = pregnancyStatus;
        this.district = district;
        this.childCount = childCount != null ? childCount : 0;
        this.isDeleted = false;
        this.termsAgreedAt = termsAgreedAt;
        this.createdAt = LocalDateTime.now();
    }

    public void updateOptionalInfo(Boolean isMultibirth, Boolean isForeigner,
        Integer residenceMonths, Integer pregnancyWeeks,
        Integer infantMonths, Integer incomeLevel,
        Boolean isHomeless) {
        this.isMultibirth = isMultibirth;
        this.isForeigner = isForeigner;
        this.residenceMonths = residenceMonths;
        this.pregnancyWeeks = pregnancyWeeks;
        this.infantMonths = infantMonths;
        this.incomeLevel = incomeLevel;
        this.isHomeless = isHomeless;
    }
}
