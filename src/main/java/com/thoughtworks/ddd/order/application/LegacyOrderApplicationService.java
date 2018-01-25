package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.OrderService;
import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderCancelled;
import com.thoughtworks.ddd.order.domain.payment.PaymentRepository;
import com.thoughtworks.ddd.order.domain.pet.PetPurchaseService;
import com.thoughtworks.ddd.order.infrastructure.messaging.MsgQueueDomainEventPublisher;
import com.thoughtworks.ddd.order.infrastructure.persistence.OrderRepositoryImpl;
import com.thoughtworks.ddd.order.infrastructure.persistence.RedisCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyOrderApplicationService {
    // remote service invoke
    // domain service
    // object factory

    @Autowired
    OrderRepositoryImpl orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PetPurchaseService petPurchaseService;
    @Autowired
    MsgQueueDomainEventPublisher domainEventPublisher;

    @Autowired
    private RedisCounter redisCounter;
    @Autowired
    private OrderService orderService;

    public boolean cancelOrder(Long orderId, String cancellationReason) {
        Order order = orderRepository.findBy(orderId);
        if (order == null) {
            return false;
        }

        if (order.notAllowToCancel()) {
            return false;
        }

        orderService.cancelOrder(order);

        //进行退货计数更新, 最多尝试3次
        redisCounter.count(orderId, cancellationReason, 3);

        //发送domain event表示 订单取消成功
        OrderCancelled orderCancelled = new OrderCancelled(orderId, cancellationReason);
        domainEventPublisher.publish(orderCancelled.toString());
        return true;
    }

}

