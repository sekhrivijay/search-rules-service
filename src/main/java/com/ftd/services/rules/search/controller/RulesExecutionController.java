package com.ftd.services.rules.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftd.services.rules.search.bl.RulesExecutionService;
import com.ftd.services.search.bl.clients.rules.RuleServiceResponse;
import com.services.micro.commons.logging.annotation.LogExecutionTime;

@RestController
@RefreshScope
@RequestMapping("/api/rules")
public class RulesExecutionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesExecutionController.class);

    private RulesExecutionService rulesExecutionService;

    @Autowired
    public void setRulesExecutionService(RulesExecutionService rulesExecutionService) {
        this.rulesExecutionService = rulesExecutionService;
    }

    @PostMapping("/executePre")
    @LogExecutionTime
    public RuleServiceResponse executePre(@RequestBody RuleServiceResponse searchModelWrapper) throws Exception {
        LOGGER.info(searchModelWrapper.toString());
        return rulesExecutionService.executePre(searchModelWrapper);
    }

    @PostMapping("/executePost")
    @LogExecutionTime
    public RuleServiceResponse executePost(@RequestBody RuleServiceResponse searchModelWrapper) throws Exception {
        LOGGER.info(searchModelWrapper.toString());
        return rulesExecutionService.executePost(searchModelWrapper);
    }
}


