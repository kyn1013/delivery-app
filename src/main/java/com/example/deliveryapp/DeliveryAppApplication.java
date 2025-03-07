package com.example.deliveryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
//@EnableJpaAuditing // 이부분 테스트 파트 메인실행시 오류가 발생하여, config 패키지의 jpaConfig.class를 별도로 만들어서 옮겨두었습니다.2
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class DeliveryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryAppApplication.class, args);
    }

}
