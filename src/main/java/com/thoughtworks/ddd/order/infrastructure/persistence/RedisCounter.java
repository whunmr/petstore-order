package com.thoughtworks.ddd.order.infrastructure.persistence;

public class RedisCounter {
    public boolean increaseCancellationCounter(Long orderId, String reason) {
        // connect to redis if need.
        // calling redis cmd: incr
        return true;
    }
}
