package com.example.villagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class VillagerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VillagerServiceApplication.class, args);
    }

    static {
        // true : 만약 해당 설정을 하지 않을 경우 서비스가 실행되는 시점에 약간의 지연이 발생하고
        // 예외 메세지가 발생합니다.
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
}
