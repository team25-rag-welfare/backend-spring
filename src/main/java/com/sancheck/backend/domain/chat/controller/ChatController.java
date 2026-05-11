package com.sancheck.backend.domain.chat.controller;

import com.sancheck.backend.domain.chat.dto.ChatHistoryDto;
import com.sancheck.backend.domain.chat.dto.request.ChatRequestDto;
import com.sancheck.backend.domain.chat.dto.response.ChatResponseDto;
import com.sancheck.backend.domain.chat.service.ChatService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        ChatResponseDto response = chatService.processRagChat(userId, request);


        return ResponseEntity.ok(response);

    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatHistoryDto>> getChatHistory(){

        //임시
        String userId = "test001";

        System.out.println("[controller] 유저" + userId + "의 과거 채팅 내역입니다.");
        List<ChatHistoryDto> history = chatService.getChatHistory(userId);
        return ResponseEntity.ok(history);
    }

    //api/v1/chats/search?keyword=어쩌구저쩌구
    @GetMapping("/search")
    public ResponseEntity<Page<ChatHistoryDto>> searchChat(
        @RequestParam String keyword,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        String userId = "test001"; // (임시 하드코딩)
        return ResponseEntity.ok(chatService.searchChatByKeyword(userId, keyword, pageable));
    }

    //api//v1/chats/date?targetDate=2026-05-11
    @GetMapping("/date")
    public ResponseEntity<Page<ChatHistoryDto>> getChatByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        String userId = "test_user_001"; // (임시 하드코딩)
        return ResponseEntity.ok(chatService.getChatByDate(userId, targetDate, pageable));
    }
}
