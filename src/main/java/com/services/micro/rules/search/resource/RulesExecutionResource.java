package com.services.micro.rules.search.resource;

import com.micro.services.search.api.SearchModelWrapper;
import com.services.micro.commons.logging.annotation.LogExecutionTime;
import com.services.micro.rules.search.bl.RulesExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/api/rules")
public class RulesExecutionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesExecutionResource.class);

    private RulesExecutionService rulesExecutionService;

    @Autowired
    public void setRulesExecutionService(RulesExecutionService rulesExecutionService) {
        this.rulesExecutionService = rulesExecutionService;
    }

    @PostMapping("/executePre")
    @LogExecutionTime
    public SearchModelWrapper executePre(@RequestBody SearchModelWrapper searchModelWrapper) throws Exception {
        LOGGER.info(searchModelWrapper.toString());
        return rulesExecutionService.executePre(searchModelWrapper);
    }

    @PostMapping("/executePost")
    @LogExecutionTime
    public SearchModelWrapper executePost(@RequestBody SearchModelWrapper searchModelWrapper) throws Exception {
        LOGGER.info(searchModelWrapper.toString());
        return rulesExecutionService.executePost(searchModelWrapper);
    }
}


