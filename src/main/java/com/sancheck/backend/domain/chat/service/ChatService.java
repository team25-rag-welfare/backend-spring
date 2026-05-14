package com.sancheck.backend.domain.chat.service;

import com.sancheck.backend.domain.chat.client.AiClientService;
import com.sancheck.backend.domain.chat.document.ChatMessage;
import com.sancheck.backend.domain.chat.dto.ChatHistoryDto;
import com.sancheck.backend.domain.chat.dto.request.ChatRequestDto;
import com.sancheck.backend.domain.chat.dto.response.AiResponseDto;
import com.sancheck.backend.domain.chat.dto.response.ChatResponseDto;
import com.sancheck.backend.domain.chat.repository.ChatMessageRepository;
import com.sancheck.backend.domain.memory.sevice.MemoryService;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemoryService memoryService;
    private final RestTemplate restTemplate = new RestTemplate(); //파이썬 ai server와 통신
    private final AiClientService aiClientService;

    //User의 질문 중 민감한 정보가 있다면 마스킹
    private String maskSensitiveInfo(String content){
        if (content == null || content.isEmpty()){
            return content;
        }
        //주민번호 마스킹
        String rrnRegex = "(\\d{6})-[0-9]{7,9}";
        content = content.replaceAll(rrnRegex, "$1-*******");

        //전화번호 마스킹
        String phoneRegex = "(\\d{2,3})-\\d{3,4}-(\\d{4})";
        content = content.replaceAll(phoneRegex, "$1-****-2");

        return content;
    }
    public ChatResponseDto processRagChat(Long userId, ChatRequestDto request) {

        //1. MySQL에서 질문한 유저 정보 꺼내오기
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("user를 찾을 수 없습니다."));
        System.out.println("질문자 정보를 확인 : " + user.getId() + "님 입니다.");

        //2. User의 민감한 정보 마스킹
        String safeContent = maskSensitiveInfo(request.getContent());


        //3. User 질문을 MongoDB에 저장
        ChatMessage userMsg = new ChatMessage();
        userMsg.setUserId(userId);
        userMsg.setSenderType("USER");
        userMsg.setContent(safeContent);
        userMsg.setDeleted(false);
        userMsg.setRegenerated(false);
        chatMessageRepository.save(userMsg);
        System.out.println("User의 질문 저장 완료");

        //4. User의 memory 긁어오기
        List<String> memoryList = memoryService.getMemoryContents(user);
        System.out.println("기존의 사용자 메모리 긁어오는데 성공" + memoryList);

        //5. User 질문 POST 및 ai 답변 받아오고 새로운 메모리 받아오기 (메모리가 있다면)
        AiResponseDto aiAnswerMemories = aiClientService.getAiResponse(user, memoryList, safeContent);
        String aiAnswer = aiAnswerMemories.answer();
        List<String> extractedNewMemories = aiAnswerMemories.newMemories();


        //6. ai의 답변을 mongoDB에 저장
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setUserId(userId);
        aiMsg.setSenderType("ASSISTANT");
        aiMsg.setContent(aiAnswer);
        aiMsg.setDeleted(false);
        aiMsg.setRegenerated(false);
        chatMessageRepository.save(aiMsg);
        System.out.println("ai 답변 저장 완료");

        //7. ai 서버에서 새로운 메모리를 보냈다면?
        memoryService.saveNewMemory(user, extractedNewMemories);

        //8. 프론트엔드로 저장물 던지기
        return ChatResponseDto.builder()
            .answer(aiAnswer)
            .senderType("ASSISTANT")
            .build();

    }

    //채팅 내역 조회 메서드
    public List<ChatHistoryDto> getChatHistory(String userId){

        //비회원이면 바로 아웃
        if (userId == null || userId.equals("guest") || userId.trim().isEmpty()){
            System.out.print("비회원은 대화 내역을 저장하지 않습니다.");
            return List.of();
        }

        //userId를 기반으로 대화 내역을 꺼낸다
        List<ChatMessage> messages = chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);

        //꺼낸 내역을 리스트로 포장
        return messages.stream()
            .map(msg -> ChatHistoryDto.builder()
                .senderType(msg.getSenderType())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt())
                .build())
            .toList();
    }

    //채팅 내역 검색
    public Page<ChatHistoryDto> searchChatByKeyword(String userId, String keyword, Pageable pageable){
        //DB에서 페이징된 결과 가져오기
        Page<ChatMessage> messagePage = chatMessageRepository.findByUserIdAndContentContainingIgnoreCase(userId, keyword, pageable);

        return messagePage
            .map(msg -> ChatHistoryDto.builder()
                .id(msg.getId())
                .senderType(msg.getSenderType())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt())
                .build());

    }

    //특정 날짜 기준 검색
    public Page<ChatHistoryDto> getChatByDate(String userId, LocalDate targetDate, Pageable pageable){
        //클릭한 날짜의 00:00부터 23:59까지로 잡는다.
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(23, 59, 59);

        Page<ChatMessage> messages = chatMessageRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay, pageable);

        return messages
            .map(msg -> ChatHistoryDto.builder()
                .id(msg.getId())
                .senderType(msg.getSenderType())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt())
                .build());

    }
}
