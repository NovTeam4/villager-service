package com.example.villagerservice.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 실행 VM 옵션에 패스워드 넣어줘야함
        // -Djasypt.encryptor.password="패스워드"
        config.setPassword(System.getProperty("jasypt.encryptor.password"));
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 암호화 알고리즘
        config.setKeyObtentionIterations(1000); // 반복할 해싱 횟수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64"); // 인코딩 방식

        encryptor.setConfig(config);
        return encryptor;
    }
}