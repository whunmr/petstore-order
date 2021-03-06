package com.thoughtworks.ddd.order.domain;

import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderCancelled;
import com.thoughtworks.ddd.order.domain.payment.Payment;
import com.thoughtworks.ddd.order.domain.payment.PaymentRepository;
import com.thoughtworks.ddd.order.domain.pet.PetPurchaseService;
import com.thoughtworks.ddd.order.infrastructure.messaging.MsgQueueDomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private PaymentRepository paymentRepository;

    public void publishCancelOrderEvent(String cancellationReason, Order order) {
        //发送domain event表示 订单取消成功
        OrderCancelled orderCancelled = new OrderCancelled(order.getId(), cancellationReason);
        new MsgQueueDomainEventPublisher().publish(orderCancelled.toString());
    }

    public boolean cancelOrder(Order order, String cancellationReason) {
        if (!order.tryCancel()){
            return false;
        }
        publishCancelOrderEvent(cancellationReason, order);

        Payment payment = this.paymentRepository.paymentOf(order.getId());
        payment.waitToRefund();

        return true;
    }
}
