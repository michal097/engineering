package com.example.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

    }

    @Async
    public void sendSimpleMail(String to, String subject, String content) {
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(to);
            helper.setReplyTo("slusarczyk.michal23@gmail.com");
            helper.setFrom("slusarczyk.michal23@gmail.com");
            helper.setSubject(subject);
            helper.setText(content, true);


        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        javaMailSender.send(msg);
    }
}
