package com.sancheck.backend.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoAuthResponseDto {
    private String access_token;
    private boolean is_new_user;
}
