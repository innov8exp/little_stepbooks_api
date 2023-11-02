package net.stepbooks.domain.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.sms.entity.Sms;
import net.stepbooks.infrastructure.enums.SmsType;

public interface SmsService extends IService<Sms> {

    void sendSms(SmsType smsType, String phone, String content);
}
