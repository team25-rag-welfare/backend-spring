package com.sancheck.backend.domain.chat.controller;

import com.sancheck.backend.domain.chat.dto.ChatHistoryDto;
import com.sancheck.backend.domain.chat.dto.request.ChatRequestDto;
import com.sancheck.backend.domain.chat.dto.response.ChatResponseDto;
import com.sancheck.backend.domain.chat.service.ChatService;
import com.sancheck.backend.global.security.CustomUserDetails;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatResponseDto> sendMessage(@AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ChatRequestDto requestDto){

        CustomUserDetails customUser = (CustomUserDetails) userDetails;
        Long userId = customUser.getUser().getId();

        return ResponseEntity.ok(chatService.processRagChat(userId, requestDto));

    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatHistoryDto>> getChatHistory(@AuthenticationPrincipal CustomUserDetails userDetails){

        Long userId = userDetails.getUser().getId();
        List<ChatHistoryDto> history = chatService.getChatHistory(userId);
        return ResponseEntity.ok(history);
    }

    //api/v1/chats/search?keyword=어쩌구저쩌구
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChatHistoryDto>> searchChat(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam String keyword,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {


        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(chatService.searchChatByKeyword(userId, keyword, pageable));
    }

    //api//v1/chats/date?targetDate=2026-05-11
    @GetMapping("/date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChatHistoryDto>> getChatByDate(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(chatService.getChatByDate(userId, targetDate, pageable));
    }
}
