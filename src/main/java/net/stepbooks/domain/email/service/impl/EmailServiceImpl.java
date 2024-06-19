package net.stepbooks.domain.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.email.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        log.info("sendSimpleEmail to={},subject={},text={}", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String text,
                                        byte[] attachment, String attachmentName) {
        log.info("sendEmailWithAttachment to={},subject={},text={},attachmentName={}",
                to, subject, text, attachmentName);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            ByteArrayResource attachmentResource = new ByteArrayResource(attachment);
            helper.addAttachment(attachmentName, attachmentResource);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.toString(), e);
        }
    }
}
