package com.sancheck.backend.domain.chat.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private String content; // User가 입력한것

    //비회원 전용
    @Builder.Default
    private List<Map<String, String>> chatHistory = new ArrayList<>();
}
