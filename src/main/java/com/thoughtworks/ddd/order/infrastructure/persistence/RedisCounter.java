package com.thoughtworks.ddd.order.infrastructure.persistence;

public class RedisCounter {
    public boolean increaseCancellationCounter(Long orderId, String reason) {
        // connect to redis if need.
        // calling redis cmd: incr
        return true;
    }

    public void count(Long orderId, String cancellationReason, int retryCounter) {
        boolean updateCounterSucceeded = false;
        int i = 0;
        while (!updateCounterSucceeded && i++ < retryCounter) {
            updateCounterSucceeded = increaseCancellationCounter(orderId, cancellationReason);
        }
    }
}
