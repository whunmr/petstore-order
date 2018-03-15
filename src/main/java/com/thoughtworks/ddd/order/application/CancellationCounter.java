package com.thoughtworks.ddd.order.application;

public interface CancellationCounter {
    public void increase(Long orderId, String cancellationReason, int retryCounter);
}
