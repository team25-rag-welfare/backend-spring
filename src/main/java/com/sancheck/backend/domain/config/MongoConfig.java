package com.sancheck.backend.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import com.mongodb.client.MongoClients;

@Configuration // "스프링아, 딴 거 다 무시하고 이 설정부터 무조건 읽어라!"
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        // 🚨 여기에 진짜 아틀라스 주소를 싹 붙여넣으십쇼!
        String myAtlasUri = "mongodb+srv://jefndb:1111@cluster0.s7uiual.mongodb.net/chat_messages?retryWrites=true&w=majority";

        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(myAtlasUri), "chat_messages"));
    }
}
