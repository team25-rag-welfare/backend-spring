package com.sancheck.backend.domain.chat.client;

import com.sancheck.backend.domain.chat.dto.response.AiResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiClientService {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(aiServerUrl).build();
    }

    public AiResponseDto getAiResponse(User user, List<String> memory, String userMessage) {
        //1. 데이터 포장
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_info", buildUserInfo(user));
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);

        return webClient.post()
                .uri("/api/v2/ai/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AiResponseDto.class)
                .block();

    }

    public AiResponseDto regenerateAiResponse(User user, List<String> memory, String userMessage,
            String previousResponse) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_info", buildUserInfo(user));
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);
        requestBody.put("regenerate", true);
        requestBody.put("previous_response", previousResponse);

        return webClient.post()
                .uri("/api/v2/ai/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AiResponseDto.class)
                .block();
    }

    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("district", user.getDistrict());
        userInfo.put("pregnancy_status", user.getPregnancyStatus());
        userInfo.put("age", user.getUserAge());
        userInfo.put("children_count", user.getChildCount());

        if (user.getDueDate() != null) {
            long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), user.getDueDate());
            userInfo.put("pregnancy_weeks", (int) ((280 - daysUntilDue) / 7));
        }
        if (user.getInfantMonths() != null) {
            userInfo.put("child_age_months", user.getInfantMonths());
        }
        if (user.getIsMultibirth() != null) {
            userInfo.put("multiple_birth", user.getIsMultibirth());
        }
        if (user.getIsForeigner() != null) {
            userInfo.put("is_korean", !user.getIsForeigner());
        }
        if (user.getIsHomeless() != null) {
            userInfo.put("no_house", user.getIsHomeless());
        }
        if (user.getIncomeLevel() != null) {
            userInfo.put("income_level", user.getIncomeLevel());
        }

        return userInfo;
    }
}
