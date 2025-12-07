package com.rate_limit.rate_limit.controller;

import com.rate_limit.rate_limit.core.FixedWindowRateLimiter;
import com.rate_limit.rate_limit.core.SlidingWindowCounterRateLimiter;
import com.rate_limit.rate_limit.core.SlidingWindowLogRateLimited;
import com.rate_limit.rate_limit.core.TokenBucketRateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class DemoController {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;
    private final SlidingWindowLogRateLimited slidingWindowLogRateLimiter;
    private final SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter;
    private final TokenBucketRateLimiter tokenBucketRateLimiter;

    @GetMapping("/test-fixed-window")
    public ResponseEntity<String> testFixedWindow(@RequestHeader(value = "X-Client", defaultValue = "Shashi") String client) {
        if(fixedWindowRateLimiter.isAllowed(client)) {
            return ResponseEntity.ok("Request successful for client: " + client);
        } else {
            return ResponseEntity.status(429).body("Rate limit exceeded for client: " + client);
        }
    }

    @GetMapping("/test-sliding-window-log")
    public ResponseEntity<String> testSlidingWindowLog(@RequestHeader(value = "X-Client", defaultValue = "Shashi") String client) {
        if(slidingWindowLogRateLimiter.isAllowed(client)) {
            return ResponseEntity.ok("Request successful for client: " + client);
        } else {
            return ResponseEntity.status(429).body("Rate limit exceeded for client: " + client);
        }
    }

    @GetMapping("/test-sliding-window-counter")
    public ResponseEntity<String> testSlidingWidowCounter(@RequestHeader(value = "X-Client", defaultValue = "Shashi") String client) {
        if(slidingWindowCounterRateLimiter.isAllowed(client)) {
            return ResponseEntity.ok("Request successful for client: " + client);
        } else {
            return ResponseEntity.status(429).body("Rate limit exceeded for client: " + client);
        }
    }

    @GetMapping("/test-token-bucket")
    public ResponseEntity<String> testBucketCounter(@RequestHeader(value = "X-Client", defaultValue = "Shashi") String client) {
        if(tokenBucketRateLimiter.isAllowed(client)) {
            return ResponseEntity.ok("Request successful for client: " + client);
        } else {
            return ResponseEntity.status(429).body("Rate limit exceeded for client: " + client);
        }
    }


}
