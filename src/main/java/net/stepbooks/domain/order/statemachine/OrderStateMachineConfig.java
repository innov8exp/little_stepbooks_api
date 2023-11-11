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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import static net.stepbooks.infrastructure.AppConstants.ORDER_STATE_MACHINE_ID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderStateMachineConfig {

    private final OrderActionService orderActionService;

    @Bean
    public StateMachine<OrderState, OrderEvent, Order> orderStateMachine() {
        StateMachineBuilder<OrderState, OrderEvent, Order> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(OrderState.INIT)
                .to(OrderState.PLACED)
                .on(OrderEvent.PLACE_SUCCESS)
                .when(checkCondition())
                .perform((from, to, event, context) -> {
                    orderActionService.saveOrderEventLog(from, to, event, context);
                    orderActionService.startPaymentTimeoutCountDown(from, to, event, context);
                });

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.PAID)
                .on(OrderEvent.PAY_SUCCESS)
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
                .from(OrderState.PAID)
                .to(OrderState.SHIPPED)
                .on(OrderEvent.SHIP_SUCCESS)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PAID)
                .to(OrderState.REFUNDING)
                .on(OrderEvent.REFUND_APPLICATION)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.REFUNDING)
                .to(OrderState.REFUNDED)
                .on(OrderEvent.REFUND_SUCCESS)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.SHIPPED)
                .to(OrderState.FINISHED)
                .on(OrderEvent.RECEIVED_SUCCESS)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.SHIPPED)
                .to(OrderState.REFUND_APPLYING)
                .on(OrderEvent.REFUND_APPLICATION)
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
                .on(OrderEvent.USER_MANUAL_CANCEL)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PLACED)
                .to(OrderState.CLOSED)
                .on(OrderEvent.ADMIN_MANUAL_CLOSE)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.PAID)
                .to(OrderState.CLOSED)
                .on(OrderEvent.ADMIN_MANUAL_CLOSE)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.SHIPPED)
                .to(OrderState.CLOSED)
                .on(OrderEvent.ADMIN_MANUAL_CLOSE)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(OrderState.SHIPPED)
                .to(OrderState.REFUNDED)
                .on(OrderEvent.SHIPPED_TO_REFUND)
                .when(checkCondition())
                .perform(doAction());



        StateMachine<OrderState, OrderEvent, Order> orderStateMachine = builder.build(ORDER_STATE_MACHINE_ID);
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
            orderActionService.saveOrderEventLog(from, to, event, context);
        };
    }

}
