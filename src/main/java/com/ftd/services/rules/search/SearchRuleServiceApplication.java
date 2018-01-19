package com.ftd.services.rules.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication // (scanBasePackages = {"com.ftd.commons"})
@EnableCaching
@EnableCircuitBreaker
@EnableHystrixDashboard
public class SearchRuleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchRuleServiceApplication.class, args);
    }
}
