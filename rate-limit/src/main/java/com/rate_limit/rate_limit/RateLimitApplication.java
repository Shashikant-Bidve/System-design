package com.rate_limit.rate_limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rate_limit.rate_limit"})
public class RateLimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitApplication.class, args);
	}

}
