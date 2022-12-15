package com.example.villagerservice.common.service;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public boolean sendMail(String email, String subject, String text){
        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8"
                );
                mimeMessageHelper.setTo(email);// 이메일 설정
                mimeMessageHelper.setSubject(subject);// 제목 설정
                mimeMessageHelper.setText(text, true);// text 설정, html 실행
            }
        };

        try{
            javaMailSender.send(msg);
            result = true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }
}
