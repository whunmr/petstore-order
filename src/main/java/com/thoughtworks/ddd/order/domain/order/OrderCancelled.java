package com.thoughtworks.ddd.order.domain.order;

import java.time.ZonedDateTime;

public class OrderCancelled {
    private Long orderId;
    private String reasonToCancel;

    private ZonedDateTime at = ZonedDateTime.now();

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setReasonToCancel(String reasonToCancel) {
        this.reasonToCancel = reasonToCancel;
    }
}
