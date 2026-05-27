package com.sancheck.backend.domain.chat.client;

import com.sancheck.backend.domain.chat.dto.response.AiResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    public AiResponseDto getAiResponse(User user, List<String> memory, String userMessage,
            List<Map<String, String>> recentChats, String currentPolicy) {
        //1. 데이터 포장
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_info", buildUserInfo(user));
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);
        requestBody.put("chat_history", recentChats != null ? recentChats : new ArrayList<>());
        requestBody.put("current_policy", currentPolicy);

        try{
            return webClient.post()
                    .uri("/api/v2/ai/chat")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(AiResponseDto.class)
                    .block();
        } catch (WebClientResponseException e){
            System.out.println("=================================================");
            System.out.println(e.getResponseBodyAsString());
            System.out.println("=================================================");
            throw e;
        }


    }

    public AiResponseDto regenerateAiResponse(User user, List<String> memory, String userMessage,
            String previousResponse, String currentPolicy) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_info", buildUserInfo(user));
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);
        requestBody.put("regenerate", true);
        requestBody.put("previous_response", previousResponse);
        requestBody.put("current_policy", currentPolicy);

        return webClient.post()
                .uri("/api/v2/ai/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AiResponseDto.class)
                .block();
    }

    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();

        //비회원은 임시 정보로 채움
        if (user == null){
            userInfo.put("district", "알수없음");
            userInfo.put("pregnancy_status", "해당없음");
            userInfo.put("age", 33); //대한민국 평균 임신 나이에 근거함(내림)
            userInfo.put("children_count", 0); //대한민국 합계출산율에 근거함(내림)
            userInfo.put("pregnancy_weeks", 0);
            userInfo.put("child_age_months", 0);
            userInfo.put("multiple_birth", false);
            userInfo.put("is_korean", true);
            userInfo.put("no_house", false);
            userInfo.put("income_level", 2400000);
            System.out.print(userInfo);
            return userInfo;
        }
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
