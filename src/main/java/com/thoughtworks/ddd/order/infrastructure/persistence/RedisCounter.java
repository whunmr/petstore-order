package com.thoughtworks.ddd.order.infrastructure.persistence;

import com.thoughtworks.ddd.order.application.CancellationCounter;

public class RedisCounter implements CancellationCounter {
    public boolean increaseCancellationCounter(Long orderId, String reason) {
        // connect to redis if need.
        // calling redis cmd: incr
        return true;
    }

    public void increase(Long orderId, String cancellationReason, int retryCounter) {
        System.out.println("retry count:" + retryCounter);
        boolean updateCounterSucceeded = false;
        int i = 0;
        while (!updateCounterSucceeded && i++ < retryCounter) {
            updateCounterSucceeded = increaseCancellationCounter(orderId, cancellationReason);
        }
    }
}
