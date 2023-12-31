package net.stepbooks.domain.order.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.service.OrderActionService;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import static net.stepbooks.infrastructure.AppConstants.VIRTUAL_ORDER_STATE_MACHINE_ID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class VirtualOrderStateMachineConfig {

    private final OrderActionService orderActionService;

    @Bean
    public StateMachine<OrderState, OrderEvent, Order> virtualOrderStateMachine() {
        StateMachineBuilder<OrderState, OrderEvent, Order> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(OrderState.INIT)
                .to(OrderState.PLACED)
                .on(OrderEvent.PLACE_SUCCESS)
                .when(checkCondition())
                .perform((from, to, event, context) -> {
                    orderActionService.updateOrderState(context, to);
                    orderActionService.startPaymentTimeoutCountDown(from, to, event, context);
                    orderActionService.saveOrderEventLog(from, to, event, context);
                });

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.PAYING)
                .on(OrderEvent.PAYMENT_SUBMIT)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PAYING)
                .to(OrderState.PLACED)
                .on(OrderEvent.PAYMENT_FAIL)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PAYING)
                .to(OrderState.FINISHED)
                .on(OrderEvent.PAYMENT_SUCCESS)
                .when(checkCondition())
                .perform((from, to, event, context) -> {
                    orderActionService.updateOrderState(context, to);
                    orderActionService.saveOrderEventLog(from, to, event, context);
                    orderActionService.updatePaymentStatus(context, PaymentStatus.PAID);
                });

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.FINISHED)
                .on(OrderEvent.PAYMENT_SUCCESS)
                .when(checkCondition())
                .perform((from, to, event, context) -> {
                    orderActionService.updateOrderState(context, to);
                    orderActionService.saveOrderEventLog(from, to, event, context);
                    orderActionService.updatePaymentStatus(context, PaymentStatus.PAID);
                });

        builder.externalTransition()
                .from(OrderState.FINISHED)
                .to(OrderState.REFUNDING)
                .on(OrderEvent.REFUND_APPROVE)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.REFUNDING)
                .to(OrderState.REFUNDED)
                .on(OrderEvent.REFUND_SUCCESS)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.CLOSED)
                .on(OrderEvent.USER_MANUAL_CANCEL)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.CLOSED)
                .on(OrderEvent.PAYMENT_TIMEOUT)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.INIT)
                .to(OrderState.CLOSED)
                .on(OrderEvent.ADMIN_MANUAL_CLOSE)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.CLOSED)
                .on(OrderEvent.ADMIN_MANUAL_CLOSE)
                .when(checkCondition())
                .perform(doAction());


        StateMachine<OrderState, OrderEvent, Order> orderStateMachine = builder.build(VIRTUAL_ORDER_STATE_MACHINE_ID);
        orderStateMachine.showStateMachine();
        String plantUML = orderStateMachine.generatePlantUML();
        log.debug("Order_State_Machine_UML: {}", plantUML);
        return orderStateMachine;
    }

    private Condition<Order> checkCondition() {
        return order -> !ObjectUtils.isEmpty(order);
    }

    private Action<OrderState, OrderEvent, Order> doAction() {
        return (from, to, event, context) -> {
            log.info("doAction: from: {}, to: {}, event: {}, context: {}", from, to, event, context);
            orderActionService.updateOrderState(context, to);
            orderActionService.saveOrderEventLog(from, to, event, context);
        };
    }

}
