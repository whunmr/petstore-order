package com.thoughtworks.ddd.order.domain.order;

import java.time.ZonedDateTime;

public class OrderCancelled {
    private final Long orderId;
    private final String reasonToCancel;
    private final ZonedDateTime at = ZonedDateTime.now();

    public OrderCancelled(Long orderId, String reasonToCancel) {
        this.orderId = orderId;
        this.reasonToCancel = reasonToCancel;
    }
}
