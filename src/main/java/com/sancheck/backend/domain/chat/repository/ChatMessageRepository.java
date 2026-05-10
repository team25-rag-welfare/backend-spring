package com.sancheck.backend.domain.chat.repository;

import com.sancheck.backend.domain.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
