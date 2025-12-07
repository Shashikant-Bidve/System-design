package com.rate_limit.rate_limit.core;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class FixedWindowRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowSizeInMillis;

    @AllArgsConstructor
    private static class Counter {
        AtomicInteger counter = new AtomicInteger(0);
        long windowStart;
    }

    private final Map<String, Counter> storage = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(String key) {
        long currentTime = System.currentTimeMillis();

        Counter counter = storage.computeIfAbsent(key, k -> new Counter(new AtomicInteger(0), currentTime));

        synchronized (counter) {
            if(currentTime - counter.windowStart >= windowSizeInMillis){
                counter.windowStart = currentTime;
                counter.counter.set(0);
            }

            if(counter.counter.incrementAndGet() <= limit){
                return true;
            }
            return false;
        }
    }

}
