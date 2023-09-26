package co.botechservices.novlnovl.domain.email.service.impl;

import co.botechservices.novlnovl.domain.email.dto.EmailDto;
import co.botechservices.novlnovl.domain.email.entity.EmailHistoryEntity;
import co.botechservices.novlnovl.domain.email.service.EmailService;
import co.botechservices.novlnovl.infrastructure.enums.EmailStatus;
import co.botechservices.novlnovl.infrastructure.enums.EmailType;
import co.botechservices.novlnovl.infrastructure.mapper.EmailHistoryMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
            emailHistoryEntity.setStatus(EmailStatus.SUCCESS);
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
