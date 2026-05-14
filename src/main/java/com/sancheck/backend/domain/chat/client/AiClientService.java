package com.sancheck.backend.domain.chat.client;

import com.sancheck.backend.domain.chat.document.ChatMessage;
import com.sancheck.backend.domain.chat.dto.response.AiResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiClientService {

    private final WebClient webClient = WebClient.builder().baseUrl("http://0.0.0.0:8000").build();

    public AiResponseDto getAiResponse(User user, List<String> memory, String userMessage){
        //1. 데이터 포장
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("age", user.getUserAge());
        userInfo.put("district", user.getDistrict());
        userInfo.put("pregnancy_weeks", user.getPregnancyWeeks());

        requestBody.put("user_info", userInfo);
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);

        //2. FastAPI로 POST 요청
        return webClient.post()
            .uri("/api/v1/ai/chat")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(AiResponseDto.class)
            .block(); //동기식
    }

}
