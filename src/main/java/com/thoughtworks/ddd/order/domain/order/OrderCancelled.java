package com.thoughtworks.ddd.order.domain.order;

import java.time.ZonedDateTime;

public class OrderCancelled {
    private Long orderId;
    private String reasonToCancel;
    private ZonedDateTime at = ZonedDateTime.now();

    public OrderCancelled(Long orderId, String reasonToCancel) {
        this.orderId = orderId;
        this.reasonToCancel = reasonToCancel;
    }
}
