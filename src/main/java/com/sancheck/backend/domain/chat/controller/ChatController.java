package com.sancheck.backend.domain.chat.controller;

import com.sancheck.backend.domain.chat.dto.request.ChatRequestDto;
import com.sancheck.backend.domain.chat.dto.response.ChatResponseDto;
import com.sancheck.backend.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequestDto request){

        String userId = "test001";

        System.out.println("임시 유저 [" + userId + "]의 질문이 도착했습니다: " + request.getContent());

        //ChatResponseDto response = chatService.processRagChat(userId, request);

        //[임시]
        ChatResponseDto tempResponse = ChatResponseDto.builder()
            .answer("ai server가 없으므로 대신 답변입니다.질문: " + request.getContent())
            .senderType("ASSISTANT")
            .createdAt("2026-05-10 15:30:00")
            .build();

        return ResponseEntity.ok(tempResponse);

    }
}
