package net.stepbooks.domain.user.service;

import net.stepbooks.domain.user.entity.UserAccountEntity;
import net.stepbooks.interfaces.client.dto.ConsumptionDto;
import net.stepbooks.interfaces.client.dto.OrderDto;

public interface UserAccountService {

    UserAccountEntity getUserAccount(String userId);

    void consumeCoin(String userId, ConsumptionDto consumptionDto);

    void rechargeCoin(String userId, OrderDto orderDto);
}
