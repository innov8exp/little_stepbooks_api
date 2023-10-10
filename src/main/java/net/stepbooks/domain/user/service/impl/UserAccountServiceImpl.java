package net.stepbooks.domain.user.service.impl;

import net.stepbooks.interfaces.client.dto.ConsumptionDto;
import net.stepbooks.interfaces.client.dto.OrderDto;
import net.stepbooks.domain.order.entity.ConsumptionEntity;
import net.stepbooks.domain.order.entity.OrderEntity;
import net.stepbooks.domain.order.service.ConsumptionService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.product.entity.ProductEntity;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.UserAccountEntity;
import net.stepbooks.domain.user.service.UserAccountService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.OrderStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.domain.user.mapper.UserAccountMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final OrderService orderService;
    private final UserAccountMapper userAccountMapper;
    private final ConsumptionService consumptionService;
    private final ProductService productService;

    public UserAccountServiceImpl(OrderService orderService, UserAccountMapper userAccountMapper,
                                  ConsumptionService consumptionService, ProductService productService) {
        this.orderService = orderService;
        this.userAccountMapper = userAccountMapper;
        this.consumptionService = consumptionService;
        this.productService = productService;
    }

    @Override
    public UserAccountEntity getUserAccount(String userId) {
        return userAccountMapper.selectOne(Wrappers.<UserAccountEntity>lambdaQuery()
                .eq(UserAccountEntity::getUserId, userId));
    }

    @Transactional
    @Override
    public void consumeCoin(String userId, ConsumptionDto consumptionDto) {
        BigDecimal coin = consumptionDto.getCoinAmount();
        UserAccountEntity userAccount = this.getUserAccount(userId);
        if (userAccount == null) {
            throw new BusinessException(ErrorCode.REDUCE_BALANCE_FAILED);
        }
        BigDecimal coinBalance = userAccount.getCoinBalance();
        if (coinBalance == null || coin.compareTo(coinBalance) > 0) {
            throw new BusinessException(ErrorCode.REDUCE_BALANCE_FAILED);
        }
        BigDecimal theNewCoinBalance = coinBalance.subtract(coin);
        userAccount.setCoinBalance(theNewCoinBalance);
        userAccountMapper.updateById(userAccount);
        ConsumptionEntity consumptionEntity = BaseAssembler.convert(consumptionDto, ConsumptionEntity.class);
        consumptionService.createConsumption(consumptionEntity);
    }

    @Transactional
    @Override
    public void rechargeCoin(String userId, OrderDto orderDto) {
        ProductEntity product = productService.findProduct(orderDto.getProductId());
        orderDto.setCoinAmount(product.getCoinAmount());
        orderDto.setTransactionAmount(product.getPrice());
        orderDto.setStatus(OrderStatus.PAID);
        BigDecimal coin = orderDto.getCoinAmount();
        UserAccountEntity userAccount = this.getUserAccount(userId);
        if (userAccount == null) {
            UserAccountEntity userAccountEntity = new UserAccountEntity();
            userAccountEntity.setUserId(userId);
            userAccountEntity.setCoinBalance(coin);
            userAccountMapper.insert(userAccountEntity);
            OrderEntity orderEntity = BaseAssembler.convert(orderDto, OrderEntity.class);
            orderService.createOrder(orderEntity);
            return;
        }
        BigDecimal coinBalance = userAccount.getCoinBalance();
        if (coinBalance == null || coinBalance.equals(BigDecimal.ZERO)) {
            userAccount.setCoinBalance(coin);
            userAccountMapper.updateById(userAccount);
            OrderEntity orderEntity = BaseAssembler.convert(orderDto, OrderEntity.class);
            orderService.createOrder(orderEntity);
            return;
        }
        BigDecimal theNewCoinBalance = coinBalance.add(coin);
        userAccount.setCoinBalance(theNewCoinBalance);
        userAccountMapper.updateById(userAccount);
        OrderEntity orderEntity = BaseAssembler.convert(orderDto, OrderEntity.class);
        orderService.createOrder(orderEntity);
    }
}
