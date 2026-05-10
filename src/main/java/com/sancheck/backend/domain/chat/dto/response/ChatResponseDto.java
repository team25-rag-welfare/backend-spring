package com.sancheck.backend.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder //데이터를 집어넣기 편하게 해줌
public class ChatResponseDto {
    private String answer; //ai 최종답변
    private String senderType; //누가 보냈나
    private String createdAt;
}
