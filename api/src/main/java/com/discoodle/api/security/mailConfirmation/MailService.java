package com.discoodle.api.security.mailConfirmation;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailService implements MailSender {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String mail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(mail, true);
            helper.setTo(to);
            helper.setSubject("Confirmez votre adresse mail.");
            helper.setFrom("hello@amigoscode.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("L'envoie du mail de confirmation n'a pas pu se faire.", e);
            throw new IllegalStateException("L'envoie du mail de confirmation n'a pas pu se faire.");
        }
    }
}

