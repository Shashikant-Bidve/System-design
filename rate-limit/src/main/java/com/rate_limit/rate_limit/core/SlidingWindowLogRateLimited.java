package com.rate_limit.rate_limit.core;

import lombok.AllArgsConstructor;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class SlidingWindowLogRateLimited implements RateLimiter{
     private final int limit;
     private final long windowSizeInMillis;

     private final ConcurrentMap<String, Deque<Long>> log = new ConcurrentHashMap<>();


    @Override
    public boolean isAllowed(String key) {
        Long currentTime = System.currentTimeMillis();
        Deque<Long> timestamps = log.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            long cutoffTime = currentTime - windowSizeInMillis;
            while(!timestamps.isEmpty() && timestamps.peekFirst() < cutoffTime) {
                timestamps.pollFirst();
            }
            if(timestamps.size() < limit) {
                timestamps.addLast(currentTime);
                return true;
            } else {
                return false;
            }
        }
    }
}
