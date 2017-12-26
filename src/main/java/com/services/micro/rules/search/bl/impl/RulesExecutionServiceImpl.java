package com.services.micro.rules.search.bl.impl;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.micro.services.search.api.SearchModelWrapper;
import com.services.micro.rules.search.bl.RulesExecutionService;
import com.services.micro.rules.search.config.RulesConfiguration;
import com.services.micro.rules.search.config.AppConfig;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

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
    public SearchModelWrapper executePre(SearchModelWrapper searchModelWrapper) throws Exception {
        return fireRules(searchModelWrapper);
    }


    @Override
    @Timed
    @ExceptionMetered
    public SearchModelWrapper executePost(SearchModelWrapper searchModelWrapper) throws Exception {
        return fireRules(searchModelWrapper);
    }


    private SearchModelWrapper fireRules(SearchModelWrapper searchModelWrapper) {
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
