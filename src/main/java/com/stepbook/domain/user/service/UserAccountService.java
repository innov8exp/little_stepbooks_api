package com.stepbook.domain.user.service;

import com.stepbook.interfaces.client.dto.ConsumptionDto;
import com.stepbook.interfaces.client.dto.OrderDto;
import com.stepbook.domain.user.entity.UserAccountEntity;

public interface UserAccountService {

    UserAccountEntity getUserAccount(String userId);

    void consumeCoin(String userId, ConsumptionDto consumptionDto);

    void rechargeCoin(String userId, OrderDto orderDto);
}
