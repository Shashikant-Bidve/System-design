package com.rate_limit.rate_limit.core;

import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class SlidingWindowCounterRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowSizeInMillis;

    @AllArgsConstructor
    private static class WindowState {
        long currWindowStart;
        long prevCount;
        long currCount;

        WindowState(long start) {
            this.currWindowStart = start;
            this.prevCount = 0;
            this.currCount = 0;
        }
    }

    private final ConcurrentHashMap<String, WindowState> counter = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(String key) {
        // Implementation goes here
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - (currentTime % windowSizeInMillis);
        WindowState state = counter.computeIfAbsent(key, k -> new WindowState(windowStart));

        synchronized (state) {
            // If we've advanced into a new fixed window, rotate windows appropriately
            if (windowStart > state.currWindowStart) {
                long diffWindows = (windowStart - state.currWindowStart) / windowSizeInMillis;
                if (diffWindows == 1) {
                    // move current -> prev, reset current
                    state.prevCount = state.currCount;
                } else {
                    // gap too large (one or more windows missed) -> reset prev
                    state.prevCount = 0;
                }
                state.currCount = 0;
                state.currWindowStart = windowStart;
            }

            // compute fraction k = fraction of current window elapsed (0..1)
            double elapsed = (double) (currentTime - state.currWindowStart);
            double k = Math.min(1.0, Math.max(0.0, elapsed / (double) windowSizeInMillis));

            // estimated requests in last window BEFORE adding this request:
            double estimatedBefore = state.currCount + state.prevCount * (1.0 - k);

            // projected estimate AFTER adding this request to current count:
            double estimatedAfter = estimatedBefore + 1.0;

            if (estimatedAfter <= (double) limit) {
                state.currCount += 1;
                return true;
            } else {
                return false;
            }
        }
    }

}
