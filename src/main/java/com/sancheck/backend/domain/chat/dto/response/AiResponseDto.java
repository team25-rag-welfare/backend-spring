package com.sancheck.backend.domain.chat.dto.response;

import java.util.List;

//FastAPI와 통신할때 응답
public record AiResponseDto(
    String answer,
    List<String> newMemories
) { }
