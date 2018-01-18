package com.ftd.services.rules.search.bl.impl;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.rules.search.bl.RulesExecutionService;
import com.ftd.services.rules.search.config.AppConfig;
import com.ftd.services.rules.search.config.RulesConfiguration;
import com.ftd.services.search.bl.clients.rules.RuleServiceResponse;

@Service(value = "RulesExecutionService")
@EnableConfigurationProperties(AppConfig.class)
public class RulesExecutionServiceImpl implements RulesExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesExecutionServiceImpl.class);

    private RulesConfiguration rulesConfiguration;

    @Autowired
    public void setRulesConfiguration(RulesConfiguration rulesConfiguration) {
        this.rulesConfiguration = rulesConfiguration;
    }


    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse executePre(RuleServiceResponse response) throws Exception {
        return fireRules(response);
    }


    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse executePost(RuleServiceResponse searchModelWrapper) throws Exception {
        return fireRules(searchModelWrapper);
    }


    private RuleServiceResponse fireRules(RuleServiceResponse searchModelWrapper) {
        KieSession knowledgeSession = null;
        try {
            knowledgeSession = rulesConfiguration.getKbase().newKieSession();
            knowledgeSession.insert(searchModelWrapper.getSearchServiceRequest());
            knowledgeSession.insert(searchModelWrapper.getSearchServiceResponse());
            knowledgeSession.fireAllRules();


            LOGGER.info("Rule execution completed ..   " + searchModelWrapper.toString());
        } catch (Throwable t) {
            LOGGER.error("Could not execute rule ", t);
        } finally {
            if (knowledgeSession != null) {
                knowledgeSession.dispose();
            }
        }
        return searchModelWrapper;
    }
}
