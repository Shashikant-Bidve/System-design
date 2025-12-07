package com.rate_limit.rate_limit.Configuration;

import com.rate_limit.rate_limit.core.FixedWindowRateLimiter;
import com.rate_limit.rate_limit.core.SlidingWindowCounterRateLimiter;
import com.rate_limit.rate_limit.core.SlidingWindowLogRateLimited;
import com.rate_limit.rate_limit.core.TokenBucketRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public FixedWindowRateLimiter fixedWindowRateLimiter() {
        return new FixedWindowRateLimiter(5, 10_000); // 5 req / 10s
    }

    @Bean
    public SlidingWindowLogRateLimited slidingWindowLogRateLimiter() {
        return new SlidingWindowLogRateLimited(5, 10_000);
    }

    @Bean
    public SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter() {
        return new SlidingWindowCounterRateLimiter(5, 10_000);
    }

    @Bean
    public TokenBucketRateLimiter tokenBucketRateLimiter() {
        return new TokenBucketRateLimiter(10, 2.0);
    }
}
