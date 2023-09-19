package co.botechservices.novlnovl.domain.user.service;

import co.botechservices.novlnovl.domain.order.dto.ConsumptionDto;
import co.botechservices.novlnovl.domain.order.dto.OrderDto;
import co.botechservices.novlnovl.domain.user.entity.UserAccountEntity;

public interface UserAccountService {

    UserAccountEntity getUserAccount(String userId);

    void consumeCoin(String userId, ConsumptionDto consumptionDto);

    void rechargeCoin(String userId, OrderDto orderDto);
}
