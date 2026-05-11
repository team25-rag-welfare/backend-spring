package com.sancheck.backend.domain.memory.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryResponseDto {
    private Long memoryId;
    private String content;
    private LocalDateTime createdAt;

}
