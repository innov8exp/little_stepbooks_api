package net.stepbooks.domain.order.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.enums.WdtSyncStatus;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.util.RandomNumberUtils;
import net.stepbooks.infrastructure.util.RedisLockUtils;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.SkuDto;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static net.stepbooks.infrastructure.AppConstants.ORDER_CODE_RANDOM_LENGTH;
import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT;

@Slf4j
@UtilityClass
public class OrderUtil {

    public static Order buildOrder(CreateOrderDto orderDto, List<SkuDto> skus, String orderCode,
                                   ProductNature productNature) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SkuDto sku : skus) {
            BigDecimal price = sku.getPrice().multiply(new BigDecimal(sku.getQuantity()));
            totalAmount = totalAmount.add(price);
        }
        return Order.builder()
                .orderCode(orderCode)
                .userId(orderDto.getUserId())
                .recipientPhone(orderDto.getRecipientPhone())
                .totalAmount(totalAmount)
                .productNature(productNature)
                .paymentStatus(PaymentStatus.UNPAID)
                .state(OrderState.INIT)
                .paymentTimeoutDuration(ORDER_PAYMENT_TIMEOUT)
                .wdtSyncStatus(WdtSyncStatus.INIT)
                .build();
    }

    // 生成订单号
    public static String generateOrderNo(String prefix) {
        // yyMMddHHmmss （下单日期时间）
        final String currentDate = FastDateFormat.getInstance("yyMMddHHmmss").format(new Date());
        // 12345(5位随机数)
        final String random = RedisLockUtils.operateWithLock(String.format("LOCK_%s", currentDate),
                () -> RandomNumberUtils.getRandom(currentDate, ORDER_CODE_RANDOM_LENGTH));
        log.info("生成订单号：{} {} {}", prefix, currentDate, random);
        return String.format("%s%s%s", prefix, currentDate, random);
    }


}
