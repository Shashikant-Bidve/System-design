package com.rate_limit.rate_limit.core;

public interface RateLimiter {
    boolean isAllowed(String key);
}
