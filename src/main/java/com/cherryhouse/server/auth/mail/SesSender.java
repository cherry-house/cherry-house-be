package com.cherryhouse.server.auth.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SesSender implements MailSender {

    @Value("${spring.mail.username}")
    private String username;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void send(String to, String code){
        Destination destination = new Destination().withToAddresses(to);
        Message message = new Message()
                .withSubject(new Content().withCharset("UTF-8").withData("메일 인증 번호입니다."))
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData("인증번호: " + code)
                ));

        try{
            amazonSimpleEmailService.sendEmail(
                    new SendEmailRequest()
                            .withSource(username)
                            .withDestination(destination)
                            .withMessage(message)
            );
        }catch (Exception e){
            throw new ApiException(ExceptionCode.EMAIL_CREATION_FAILED);
        }
    }
}
