package com.thoughtworks.ddd.order.application;

import com.thoughtworks.ddd.order.application.dto.OrderDTO;
import com.thoughtworks.ddd.order.domain.order.Order;
import com.thoughtworks.ddd.order.domain.order.OrderRepository;
import com.thoughtworks.ddd.order.domain.order.Pet;
import com.thoughtworks.ddd.order.domain.payment.PayOrderService;
import com.thoughtworks.ddd.order.domain.payment.Payment;
import com.thoughtworks.ddd.order.domain.payment.PaymentRepository;
import com.thoughtworks.ddd.order.domain.payment.PaymentStatus;
import com.thoughtworks.ddd.order.domain.pet.PetPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderApplicationService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private PetPurchaseService petPurchaseService;
    @Autowired private PayOrderService payOrderService;
    @Autowired private PaymentRepository paymentRepository;

    public Order bookPet(OrderDTO orderCommand) {
        Pet pet = orderCommand.getPet();
        petPurchaseService.lockPetOfOrder(pet.getPetId());

        Order order = new Order(orderCommand.getCustomer(), orderCommand.getShop(), pet);
        orderRepository.save(order);

        Payment payment = new Payment(order.getId(), PaymentStatus.UNPAID);
        paymentRepository.save(payment);
        return order;
    }

    public void payOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        Payment payment = payOrderService.payOrder(order);
        paymentRepository.save(payment);
        order.completed();
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.canceled();
        Payment payment = paymentRepository.paymentOf(orderId);
        payment.waitToRefund();
        petPurchaseService.Return(order.getPet().getPetId());
    }


}
