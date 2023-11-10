package net.stepbooks.domain.email.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.stepbooks.domain.email.entity.EmailHistoryEntity;
import net.stepbooks.domain.email.mapper.EmailHistoryMapper;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.infrastructure.enums.SendStatus;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.interfaces.client.dto.EmailDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    public static final long VERIFICATION_CODE_VALID_SECONDS = 60;
    public static final String EMAIL_FROM_ADDRESS = "noreply@novlnovl.com";

    private final JavaMailSender sender;
    private final EmailHistoryMapper emailHistoryMapper;

    public EmailServiceImpl(JavaMailSender sender, EmailHistoryMapper emailHistoryMapper) {
        this.sender = sender;
        this.emailHistoryMapper = emailHistoryMapper;
    }

    @Override
    public void sendSimpleMessage(EmailDto emailDto) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(emailDto.getSubject());
            helper.addTo(emailDto.getTo());
            helper.setFrom(EMAIL_FROM_ADDRESS);
            helper.setText(emailDto.getText(), true);
            sender.send(helper.getMimeMessage());
            EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
            emailHistoryEntity.setEmail(emailDto.getTo());
            emailHistoryEntity.setEmailType(emailDto.getEmailType());
            emailHistoryEntity.setVerificationCode(emailDto.getCode());
            emailHistoryEntity.setStatus(SendStatus.SUCCESS);
            emailHistoryEntity.setValidSeconds(VERIFICATION_CODE_VALID_SECONDS);
            emailHistoryEntity.setCreatedAt(LocalDateTime.now());
            emailHistoryMapper.insert(emailHistoryEntity);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean verifyValidationCode(String email, String code, EmailType emailType) {
        List<EmailHistoryEntity> emailHistoryEntities = emailHistoryMapper.selectList(Wrappers
                .<EmailHistoryEntity>lambdaQuery()
                .eq(EmailHistoryEntity::getEmail, email)
                .eq(EmailHistoryEntity::getVerificationCode, code)
                .eq(EmailHistoryEntity::getEmailType, emailType)
                .orderByDesc(EmailHistoryEntity::getCreatedAt)
        );
        if (!ObjectUtils.isEmpty(emailHistoryEntities)) {
            EmailHistoryEntity emailHistoryEntity = emailHistoryEntities.get(0);
            LocalDateTime createdAt = emailHistoryEntity.getCreatedAt();
            Long validSeconds = emailHistoryEntity.getValidSeconds();
            LocalDateTime now = LocalDateTime.now();
            // The validation code is still valid
            return createdAt.plusSeconds(validSeconds).isAfter(now);
        }
        return false;
    }

}
