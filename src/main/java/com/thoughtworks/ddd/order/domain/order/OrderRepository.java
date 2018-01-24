package com.thoughtworks.ddd.order.domain.order;

public interface OrderRepository {
    Order findBy(Long id);

    Order save(Order order);
}
