package com.thoughtworks.ddd.order.domain;

import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.payment.Payment;
import com.thoughtworks.ddd.order.domain.payment.PaymentRepository;
import com.thoughtworks.ddd.order.domain.pet.PetPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hmwang on 25/01/2018.
 */
@Service
public class OrderService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PetPurchaseService petPurchaseService;

    public void cancelOrder(Order order) {
        order.cancel();

        Payment payment = this.paymentRepository.paymentOf(order.getId());
        payment.waitToRefund();
        this.petPurchaseService.Return(order.getPet().getPetId());
    }
}
