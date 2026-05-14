package com.sancheck.backend.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoAuthRequestDto {
    private String auth_code;
}
