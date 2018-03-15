package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.OrderService;
import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderRepository;
import com.thoughtworks.ddd.order.domain.pet.PetPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyOrderApplicationService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CancellationCounter cancellationCounter;

    @Autowired
    PetPurchaseService petPurchaseService;


    public boolean cancelOrder(Long orderId, String cancellationReason) {
        Order order = orderRepository.findBy(orderId);
        if (order == null) {
            return false;
        }

        if (orderService.cancelOrder(order, cancellationReason))
            return false;

        petPurchaseService.forSale(order.getPet().getPetId());

        //进行退货计数更新, 最多尝试3次
        cancellationCounter.increase(orderId, cancellationReason, 3);

        return true;
    }

}

