package com.example.url_shortne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlMapping {
    long id;
    String shortCode;
    String originalUrl;
    Instant createdAt;
    long clickCount;
    String createdBy;

    public void incrementClickCount() {
        this.clickCount++;
    }
}
