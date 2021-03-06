package com.thoughtworks.ddd.order.interfaces.facade;

import com.thoughtworks.ddd.order.application.OrderApplicationService;
import com.thoughtworks.ddd.order.application.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/orders")
public class OrderStatusFacade extends HttpFacadeBaseClass {

    private final OrderApplicationService orderApplicationService;


    @Autowired
    public OrderStatusFacade(OrderApplicationService orderApplicationService, OrderQueryService orderQueryService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping("/{id}/status/tryCancel")
    @ResponseStatus(HttpStatus.CREATED)
    public final void createOrder(@PathVariable("id") final long id) {
        orderApplicationService.payOrder(id);
    }
}

