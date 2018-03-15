package com.thoughtworks.ddd.order.application;

public interface CancellationCounter {
    public void increaseCancelCount(Long orderId, String cancellationReason, int retryCounter);
}
