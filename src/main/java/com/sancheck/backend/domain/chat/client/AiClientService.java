package com.sancheck.backend.domain.chat.client;

import com.sancheck.backend.domain.chat.dto.response.AiResponseDto;
import com.sancheck.backend.domain.user.entity.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        // 필수
        userInfo.put("district", user.getDistrict());
        userInfo.put("pregnancy_status", user.getPregnancyStatus());
        userInfo.put("age", user.getUserAge());
        userInfo.put("children_count", user.getChildCount());

        // 선택
        if (user.getDueDate() != null) {
            long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), user.getDueDate());
            int pregnancyWeeks = (int) ((280 - daysUntilDue) / 7);
            userInfo.put("pregnancy_weeks", pregnancyWeeks);
        }
        if (user.getInfantMonths() != null)
            userInfo.put("child_age_months", user.getInfantMonths());
        if (user.getIsMultibirth() != null)
            userInfo.put("multiple_birth", user.getIsMultibirth());
        if (user.getIsForeigner() != null)
            userInfo.put("is_korean", !user.getIsForeigner());
        if (user.getIsHomeless() != null)
            userInfo.put("no_house", user.getIsHomeless());
        if (user.getIncomeLevel() != null)
            userInfo.put("income_level", user.getIncomeLevel());

        requestBody.put("user_info", userInfo);
        requestBody.put("memory", memory);
        requestBody.put("user_message", userMessage);

        //2. FastAPI로 POST 요청
        return webClient.post()
                .uri("/api/v2/ai/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AiResponseDto.class)
                .block();


    }

}
