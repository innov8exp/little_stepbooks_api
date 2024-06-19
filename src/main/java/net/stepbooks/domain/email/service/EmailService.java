package net.stepbooks.domain.email.service;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

    void sendEmailWithAttachment(String to, String subject, String text,
                                 byte[] attachment, String attachmentName);
}
