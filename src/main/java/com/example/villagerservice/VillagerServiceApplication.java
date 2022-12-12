package com.example.villagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VillagerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VillagerServiceApplication.class, args);
    }
}
