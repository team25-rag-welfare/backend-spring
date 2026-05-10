package com.sancheck.backend.domain.chat.service;

import com.sancheck.backend.domain.chat.document.ChatMessage;
import com.sancheck.backend.domain.chat.dto.request.ChatRequestDto;
import com.sancheck.backend.domain.chat.dto.response.ChatResponseDto;
import com.sancheck.backend.domain.chat.repository.ChatMessageRepository;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RestTemplate restTemplate = new RestTemplate(); //파이썬 ai server와 통신

    public ChatResponseDto processRagChat(String userId, ChatRequestDto request) {

        //1. MySQL에서 질문한 유저 정보 꺼내오기
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("user를 찾을 수 없습니다."));
        System.out.println("질문자 정보를 확인 : " + user.getUserAge() + "살!!");

        //2. User 질문을 MongoDB에 저장
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSenderType("USER");
        userMsg.setContent(request.getContent());
        userMsg.setDeleted(false);
        userMsg.setRegenerated(false);
        chatMessageRepository.save(userMsg);
        System.out.println("User의 질문 저장 완료");

        //3. 파이썬 ai 서버로 질문 및 유저 조건 json 포장
        String pythonServerUrl = "http://localhost:8000/api/ai/chat";
        Map<String, Object> aiRequest = new HashMap<>();
        aiRequest.put("question", request.getContent());
        aiRequest.put("age", user.getUserAge()); //ai가 몇 살의 사람에게 줄 혜택을 찾아 던져줌

        String aiAnswer = "";
        try{
            System.out.println("ai 서버로 요청중");
            ResponseEntity<String> response = restTemplate.postForEntity(pythonServerUrl, aiRequest, String.class);
            aiAnswer = response.getBody();
        } catch (Exception e){
            System.out.println("ai 서버 응답 없음");
            aiAnswer = "현재 AI 서버 점검 중입니다.";
        }

        //4. ai의 답변을 mongoDB에 저장
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setSenderType("ASSISTANT");
        aiMsg.setContent(aiAnswer);
        aiMsg.setDeleted(false);
        aiMsg.setRegenerated(false);
        chatMessageRepository.save(aiMsg);
        System.out.println("ai 답변 저장 완료");

        //5. 프론트엔드로 저장물 던지기
        return ChatResponseDto.builder()
            .answer(aiAnswer)
            .senderType("ASSISTANT")
            .build();
    }
}
