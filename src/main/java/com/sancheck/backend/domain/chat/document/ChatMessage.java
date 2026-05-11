package com.sancheck.backend.domain.chat.document;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    @Field("message_id")
    private String id;

    private String userId;

    @Field("sender_type")
    private String senderType;

    private String content;

    @Field("is_regenerated")
    private boolean isRegenerated;

    @Field("is_deleted")
    private boolean isDeleted;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;


}
