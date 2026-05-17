package com.sancheck.backend.domain.chat.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

//프론트엔드로 보내줄 때 응답
@Getter
@Builder //데이터를 집어넣기 편하게 해줌
public class ChatResponseDto {
    private String answer; //ai 최종답변
    private String senderType; //누가 보냈나
    private String createdAt;
}
