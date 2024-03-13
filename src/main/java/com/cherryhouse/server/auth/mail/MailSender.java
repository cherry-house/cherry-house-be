package com.cherryhouse.server.auth.mail;

public interface MailSender {

    void send(String to, String code);
}
