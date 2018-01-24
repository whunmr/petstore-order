package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderCancelled;
import com.thoughtworks.ddd.order.domain.order.OrderRepository;
import com.thoughtworks.ddd.order.domain.order.OrderStatus;
import com.thoughtworks.ddd.order.domain.payment.Payment;
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

    @Autowired OrderRepositoryImpl orderRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired PetPurchaseService petPurchaseService;
    @Autowired MsgQueueDomainEventPublisher domainEventPublisher;

    @Autowired
    private RedisCounter redisCounter;

    public boolean cancelOrder(Long orderId, String cancellationReason) {
        Order order = orderRepository.findBy(orderId);
        if (order == null) {
            return false;
        }

        //业务政策：蚂蚁，鱼等生命力不强的动物无法取消
        if (order.getCategory() == "Fish" || order.getCategory() == "Ant") {
            return false;
        }

        //如果订单已经取消 或 已经关闭，则不能继续取消       //domain business rule
        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.CLOSED) {
            return false;
        }

        //如果订单已经收货，则不能取消
        order.setOrderStatus(OrderStatus.CANCELLED);  //anemic domain model, state change and setter

        Payment payment = paymentRepository.paymentOf(orderId);
        payment.waitToRefund();
        petPurchaseService.Return(order.getPet().getPetId());

        //进行退货计数更新, 最多尝试3次
        boolean updateCounterSucceeded = false;
        int i = 0;
        while (!updateCounterSucceeded && i++ < 3) {
            updateCounterSucceeded = redisCounter.increaseCancellationCounter(orderId, cancellationReason);
        }

        //发送domain event表示 订单取消成功
        OrderCancelled orderCancelled = new OrderCancelled();
        orderCancelled.setOrderId(orderId);
        orderCancelled.setReasonToCancel(cancellationReason);
        domainEventPublisher.publish(orderCancelled.toString());

        return true;
    }
}

