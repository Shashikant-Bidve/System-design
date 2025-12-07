package com.rate_limit.rate_limit.core;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class TokenBucketRateLimiter implements RateLimiter {
    private final int capacity;
    private final double refillRatePerSecond;

    @AllArgsConstructor
    private static class Bucket {
        double tokens;
        long lastRefillTimestamp;
    }

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(String key) {
        long currentTime = System.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket(capacity, currentTime));

        synchronized (bucket) {
            // Refill tokens based on elapsed time
            long elapsedTime = currentTime - bucket.lastRefillTimestamp;
            double tokensToAdd = (elapsedTime / 1000.0) * refillRatePerSecond;
            bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
            bucket.lastRefillTimestamp = currentTime;

            if (bucket.tokens >= 1) {
                bucket.tokens -= 1;
                return true;
            } else {
                return false;
            }
        }
    }


}
