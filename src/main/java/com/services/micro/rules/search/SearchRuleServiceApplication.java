package com.services.micro.rules.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableCaching
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableConfigServer
public class SearchRuleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchRuleServiceApplication.class, args);
    }

}
