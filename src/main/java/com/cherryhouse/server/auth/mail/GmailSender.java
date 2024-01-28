package com.cherryhouse.server.auth.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
public class GmailSender implements MailSender {

    private final JavaMailSender javaMailSender;

    public void send(String to, String code){
        System.out.println("ggmail sender");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("메일 인증 번호입니다.");
        message.setText("인증번호: " + code);
        javaMailSender.send(message);
    }
}
