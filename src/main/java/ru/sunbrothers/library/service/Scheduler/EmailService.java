package ru.sunbrothers.library.service.Scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    void sendMail(String to, String subject, String text){
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setTo(to);
            smm.setSubject(subject);
            smm.setText(text);
            mailSender.send(smm);
        } catch (MailException e) {
            log.error(e.getMessage());
        }
    }
}
