package com.example.villagerservice.common.service;

import static com.example.villagerservice.common.exception.MailErrorCode.*;

import com.example.villagerservice.common.exception.MailErrorCode;
import com.example.villagerservice.common.exception.MailException;
import java.util.ArrayList;
import java.util.Random;
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

    /**
     * 이메일 전송
     * @param email
     * @param subject
     * @param text
     */
    public void sendMail(String email, String subject, String text){
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
        }catch(Exception e){
            throw new MailException(FAILED_SEND_MASSAGE);
        }
    }

    /**
     * 6자리 인증번호 생성
     * @return
     */
    public static String createKey() {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            list.add((rand.nextInt(10)));
        }
        return list.toString();
    }

    /**
     * 회원가입 이메일 인증
     */
    public String signupEmailCert(String email){
        // 인증번호생성
        String key = createKey();
        // 제목생성
        String subject = "동네#람들 인증 번호 6자리입니다.";
        String text = "<p>동네#람들 인증 번호 6자리입니다.</p>" +
                            "<p>" + key + "</p>";
        // 이메일전송
        sendMail(email, subject, text);

        // 키 반환
        return key;
    }
}
