package com.sancheck.backend.domain.chat.dto.response;

import com.sancheck.backend.domain.chat.dto.response.AiResponseDto.PolicyAnswer;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

//프론트엔드로 보내줄 때 응답
@Getter
@Builder //데이터를 집어넣기 편하게 해줌
public class ChatResponseDto {

    private List<PolicyAnswer> policies;
    private String senderType; //누가 보냈나
    private String createdAt;
    private String messageId;

    @Getter
    @Builder
    public static class PolicyAnswer {
        private String policyName;
        private String content;
    }
}
