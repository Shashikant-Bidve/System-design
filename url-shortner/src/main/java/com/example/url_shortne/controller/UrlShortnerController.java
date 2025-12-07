package com.example.url_shortne.controller;

import com.example.url_shortne.model.UrlMapping;
import com.example.url_shortne.service.ShortnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UrlShortnerController {

    private final ShortnerService shorterService;

    @PostMapping("/api/url/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String url, @RequestHeader(value = "X-User", defaultValue = "anonymous") String user) {
        if(url == null || url.isEmpty()){
            return ResponseEntity.badRequest().body("URL cannot be empty");
        }
        String shortUrl = shorterService.shortenUrl(url, user);
        return ResponseEntity.ok("https://short.ly/u/" + shortUrl);
    }

    @GetMapping("/api/url/{shortCode}")
    public ResponseEntity<String> shortUrl(@PathVariable String shortCode) {
        if (shortCode == null || shortCode.isEmpty()) {
            return ResponseEntity.badRequest().body("Short URL cannot be empty");
        }
        String originalUrl = shorterService.resolveUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).body(originalUrl);
    }

    @GetMapping("/api/url/info/{shortCode}")
    public ResponseEntity<UrlMapping> getUrlInfo(@PathVariable String shortCode) {
        if (shortCode == null || shortCode.isEmpty()) {
            return ResponseEntity.badRequest().body(new UrlMapping());
        }
        UrlMapping info = shorterService.getUrlInfo(shortCode);
        return ResponseEntity.ok(info);
    }

}
