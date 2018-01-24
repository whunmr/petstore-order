package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderRepository;
import com.thoughtworks.ddd.order.domain.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyOrderApplicationService {
    // anemic domain model， state change and setter
    // domain business rule
    // business policy
    // remote service invoke
    // infrastructure invocation
    // domain service
    // object factory
    // repository
    // domain event

    @Autowired private OrderRepository orderRepository;

    public boolean cancelOrder(Long orderId, String cancellationReason) {
        //Payment payment = paymentRepository.paymentOf(orderId);
        //payment.waitToRefund();
        //petPurchaseService.Return(order.getPet().getPetId());

        //如果订单不存在，通过异常返回
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            return false;
        }

        //存在充值卡等电子商品无法退货政策
        if (order.getCategory() == "digital top-up card") {
            return false;
        }

        //如果订单已经取消 或 已经关闭，则不能继续取消       //domain business rule
        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.CLOSED) {
            return false;
        }

        //如果订单已经收货，则不能取消
        order.setOrderStatus(OrderStatus.CANCELLED);  //anemic domain model, state change and setter

        //发送domain event表示 取消失败
        //发送domain event表示 取消成功
        return true;
    }
}

