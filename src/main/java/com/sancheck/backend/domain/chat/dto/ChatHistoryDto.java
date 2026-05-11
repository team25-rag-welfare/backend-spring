package com.sancheck.backend.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatHistoryDto {
    private String id;
    private String senderType;
    private String content;
    private LocalDateTime createdAt;
}
