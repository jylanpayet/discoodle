package com.discoodle.api.security.mailConfirmation;

public interface MailSender {
    void send(String to, String mail);
}
