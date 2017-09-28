package com.services.micro.rules.search.hc;

import com.services.micro.rules.search.api.response.RuleServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {


    private RulesService rulesService;

    @Autowired
    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Health health() {
        RuleServiceResponse ruleServiceResponse = rulesService.getResponse("test");
//        if (ruleServiceResponse.getMessage().equals("Hello test")) {
            return Health.up().build();
//        }
//        return Health
//                .down()
//                .withDetail("Error Code", 1)
//                .withException(new Exception("rulesService could not return result"))
//                .build();
    }
}
