package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.OrderService;
import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.infrastructure.messaging.MsgQueueDomainEventPublisher;
import com.thoughtworks.ddd.order.infrastructure.persistence.OrderRepositoryImpl;
import com.thoughtworks.ddd.order.infrastructure.persistence.RedisCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyOrderApplicationService {
    @Autowired
    OrderRepositoryImpl orderRepository;
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

        if (orderService.cancelOrder(order, cancellationReason))
            return false;

        //进行退货计数更新, 最多尝试3次
        redisCounter.count(orderId, cancellationReason, 3);

        return true;
    }

}

