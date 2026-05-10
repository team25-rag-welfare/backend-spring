package com.sancheck.backend.domain.config;

import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyDataInit {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository){
        return args -> {
            // 서버 켜질 때 "test_user_001"이 DB에 없으면 하나 새로 만든다!
            if (!userRepository.existsById("test_user_001")) {
                User dummyUser = new User();
                dummyUser.setUserId("testUser001");
                dummyUser.setUserAge(10);
                dummyUser.setPregnant(false);
                dummyUser.setChildAge(0);

                userRepository.save(dummyUser);
                System.out.println("가짜 유저 생성완료");
            }
        };
    }
}
