package com.example.url_shortne.service;

import com.example.url_shortne.model.UrlMapping;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import static com.example.url_shortne.util.Base62EncoderDecoder.encode;

@Component
public class ShortnerService {

    private final AtomicLong idGen = new AtomicLong(1);
    private final ConcurrentHashMap<String, UrlMapping> db = new ConcurrentHashMap<>();

    public String shortenUrl(String url, String user) {
        if(url == null || url.isEmpty()){
            throw new IllegalArgumentException("URL cannot be empty");
        }
        long id = idGen.getAndIncrement();
        String encodedUrl = encode(id);
        UrlMapping urlMapping = new UrlMapping(id, encodedUrl, url, Instant.now(), 0, user);
        db.putIfAbsent(encodedUrl, urlMapping);
        return encodedUrl;
    }

    public String resolveUrl(String shortCode) {
        if(shortCode == null || shortCode.isEmpty()){
            throw new IllegalArgumentException("Short URL cannot be empty");
        }
        UrlMapping mapping = db.get(shortCode);
        if (mapping == null) {
            throw new IllegalArgumentException("Short URL does not exist");
        }
        String originalUrl = mapping.getOriginalUrl();
        if(originalUrl == null || originalUrl.isEmpty()){
            throw new IllegalArgumentException("Short URL does not exist");
        }
        db.get(shortCode).incrementClickCount();
        return originalUrl;
    }

    public UrlMapping getUrlInfo(String shortCode) {
        if(shortCode == null || shortCode.isEmpty()){
            throw new IllegalArgumentException("Short URL cannot be empty");
        }
        return db.getOrDefault(shortCode, null);
    }
}
