package com.sancheck.backend.domain.config;

import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyDataInit {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository){
        return args -> {
            // 서버 켜질 때 1번 사용자가 DB에 없으면 하나 새로 만든다!
            if (!userRepository.existsById(1L)) {
                User dummyUser = User.builder()
                    .socialId("test001")
                    .socialType("kakao")
                    .email("jinujjang@kakao.com")
                    .userName("최진우")
                    .userAge(20)
                    .pregnancyStatus("...")
                    .district("서울시 은평구")
                    .childCount(0)
                    .termsAgreedAt(LocalDateTime.now())
                    .build();

                userRepository.save(dummyUser);
                System.out.println("가짜 유저 생성완료");
            }
        };
    }
}
