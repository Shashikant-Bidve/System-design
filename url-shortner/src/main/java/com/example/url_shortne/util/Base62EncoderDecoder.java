package com.example.url_shortne.util;

public class Base62EncoderDecoder {
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            int remainder = (int) (value % 62);
            sb.append(BASE62.charAt(remainder));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result * 62 + BASE62.indexOf(str.charAt(i));
        }
        return result;
    }
}
