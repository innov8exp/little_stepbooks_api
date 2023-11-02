package net.stepbooks.domain.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.sms.entity.Sms;
import net.stepbooks.domain.sms.mapper.SmsMapper;
import net.stepbooks.domain.sms.service.SmsService;
import net.stepbooks.infrastructure.enums.SendStatus;
import net.stepbooks.infrastructure.enums.SmsSignType;
import net.stepbooks.infrastructure.enums.SmsType;
import net.stepbooks.infrastructure.external.client.SmsClient;
import net.stepbooks.infrastructure.external.dto.SmsSendRequest;
import net.stepbooks.infrastructure.external.dto.SmsSendResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService {

    @Value("${sms.account}")
    private String account;
    @Value("${sms.password}")
    private String password;
    private final SmsClient smsClient;

    @Override
    public void sendSms(SmsType smsType, String phone, String content) {
        SmsSendRequest smsSendRequest = SmsSendRequest.builder()
                .nonce(String.valueOf(Math.random()))
                .timestamp(DateTimeUtils.currentTimeMillis())
                .account(account)
                .signType(SmsSignType.NORMAL.getValue())
                .signature(password)
                .param(SmsSendRequest.Param.builder()
                        .mobile(phone)
                        .content(content)
                        .build()
                )
                .build();
        ResponseEntity<SmsSendResponse> response = smsClient.send(smsSendRequest);
        SmsSendResponse smsSendResponse = response.getBody();
        if (ObjectUtils.isNotEmpty(smsSendResponse)) {
            SmsSendResponse.Data data = smsSendResponse.getData();
            Long msgId = data.getMsgId();
            Sms sms = Sms.builder()
                    .phone(phone)
                    .content(content)
                    .msgId(msgId)
                    .smsType(smsType)
                    .status(SendStatus.SUCCESS)
                    .build();
            this.save(sms);
        }
    }
}
