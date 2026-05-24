package com.sancheck.backend.domain.chat.document;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private Long userId;

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
