package com.sancheck.backend.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoAuthResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("is_new_user")
    private boolean isNewUser;
}
