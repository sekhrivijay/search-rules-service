package com.services.micro.rules.search.hc;

import com.micro.services.search.api.SearchModelWrapper;
import com.services.micro.rules.search.bl.RulesExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RuleExecutionHealthCheck implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExecutionHealthCheck.class);
    private RulesExecutionService rulesExecutionService;
    private static final SearchModelWrapper SEARCH_MODEL_WRAPPER = new SearchModelWrapper(null, null);

    @Autowired
    public void setRulesExecutionService(RulesExecutionService rulesExecutionService) {
        this.rulesExecutionService = rulesExecutionService;
    }

    @Override
    public Health health() {
        try {
            rulesExecutionService.executePre(SEARCH_MODEL_WRAPPER);
        } catch (Exception e) {
            LOGGER.error("Health check failed ... ", e);
            return Health
                    .down()
                    .withDetail("Error Code", 1)
                    .withException(e)
                    .build();

        }
        return Health.up().build();
    }
}
