package net.stepbooks.domain.email.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.email.entity.EmailHistoryEntity;
import net.stepbooks.domain.email.mapper.EmailHistoryMapper;
import net.stepbooks.domain.email.service.EmailBusinessService;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.enums.SendStatus;
import net.stepbooks.interfaces.client.dto.EmailDto;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailBusinessServiceImpl implements EmailBusinessService {

    public static final long VERIFICATION_CODE_VALID_SECONDS = 60;

    private final EmailHistoryMapper emailHistoryMapper;

    private final EmailService emailService;

    @Override
    public void sendSimpleMessage(EmailDto emailDto) {

        emailService.sendSimpleEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getText());

        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setEmail(emailDto.getTo());
        emailHistoryEntity.setEmailType(emailDto.getEmailType());
        emailHistoryEntity.setVerificationCode(emailDto.getCode());
        emailHistoryEntity.setStatus(SendStatus.SUCCESS);
        emailHistoryEntity.setValidSeconds(VERIFICATION_CODE_VALID_SECONDS);
        emailHistoryEntity.setCreatedAt(LocalDateTime.now());
        emailHistoryMapper.insert(emailHistoryEntity);
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
