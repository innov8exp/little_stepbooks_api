package net.stepbooks.domain.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.mapper.PaymentMapper;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOpsServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentOpsService {

}
