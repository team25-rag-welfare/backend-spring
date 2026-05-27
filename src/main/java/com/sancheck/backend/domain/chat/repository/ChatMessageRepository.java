package com.sancheck.backend.domain.chat.repository;

import com.sancheck.backend.domain.chat.document.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByUserIdOrderByCreatedAtAsc(Long userId);

    void deleteByUserId(Long userId);
    void deleteByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    //키워드 검색 (대소문자 무시)
    Page<ChatMessage> findByUserIdAndContentContainingIgnoreCase(Long userId, String keyword, Pageable pageable);


    //날짜별 검색 '시작 시간'과 '끝 시간' 사이에 있는 것만 검색
    Page<ChatMessage> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    // chatId보다 작은 USER 메시지 중 가장 최근 것
    Optional<ChatMessage> findTopByUserIdAndSenderTypeAndIdLessThanOrderByIdDesc(Long userId, String senderType, String id);

    //가장 최근 대화 2개
    List<ChatMessage> findTop2ByUserIdOrderByCreatedAtDesc(Long userId);
}
