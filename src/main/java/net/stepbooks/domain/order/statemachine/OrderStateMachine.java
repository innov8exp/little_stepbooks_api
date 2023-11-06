package net.stepbooks.domain.order.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import net.stepbooks.domain.order.entity.OrderEntity;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;

public class OrderStateMachine {

    public void createStateMachine() {
        StateMachineBuilder<OrderState, OrderEvent, OrderEntity> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(OrderState.INIT)
                .to(OrderState.PLACED)
                .on(OrderEvent.PLACE_SUCCESS)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<OrderState, OrderEvent, OrderEntity> orderStateMachine
                = builder.build("orderStateMachine");
        OrderState orderState
                = orderStateMachine.fireEvent(OrderState.INIT, OrderEvent.PLACE_SUCCESS, new OrderEntity());
        assert orderState == OrderState.PLACED;
    }

    private Condition<OrderEntity> checkCondition() {
        return orderEntity -> true;
    }

    private Action<OrderState, OrderEvent, OrderEntity> doAction() {
        System.out.println("doAction");
        return (from, to, event, context) -> {
            System.out.println("from: " + from);
            System.out.println("to: " + to);
            System.out.println("event: " + event);
            System.out.println("context: " + context);
        };
    }

}
