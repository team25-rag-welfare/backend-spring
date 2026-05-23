package com.sancheck.backend.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

//FastAPI와 통신할때 응답
public record AiResponseDto(
        List<PolicyAnswer> policies,
        @JsonProperty("new_memories") List<String> newMemories
) {
    public record PolicyAnswer(
            @JsonProperty("policy_name") String policyName,
            String content
    ) {}
}