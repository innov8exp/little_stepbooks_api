package net.stepbooks.domain.user.service;

import net.stepbooks.interfaces.client.dto.ConsumptionDto;
import net.stepbooks.interfaces.client.dto.OrderDto;
import net.stepbooks.domain.user.entity.UserAccountEntity;

public interface UserAccountService {

    UserAccountEntity getUserAccount(String userId);

    void consumeCoin(String userId, ConsumptionDto consumptionDto);

    void rechargeCoin(String userId, OrderDto orderDto);
}
