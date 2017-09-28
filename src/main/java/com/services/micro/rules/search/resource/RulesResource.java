package com.services.micro.rules.search.resource;

import com.services.micro.rules.search.api.request.RuleServiceRequest;
import com.services.micro.rules.search.api.response.RuleServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequestMapping("/rules")
public class RulesResource {

    private RulesService rulesService;

    @Autowired
    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping("/")
    public RuleServiceResponse createRule(@RequestBody RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.create(ruleServiceRequest);
    }

    @GetMapping("/")
    public RuleServiceResponse readRule(RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.read(ruleServiceRequest);
    }

    @GetMapping("/test")
    public RuleServiceRequest testRule(RuleServiceRequest ruleServiceRequest) throws Exception {
        return RuleServiceRequest.RuleServiceRequestBuilder
                .aRuleServiceRequest()
                .withEnvironment("testend")
                .withPackageName("testttpack")
                .withRuleName("My rule name")
                .withRule("My rule ..")
                .withServiceName("my srevice name")
                .build();
//        return rulesService.read(ruleServiceRequest);
    }

    @DeleteMapping("/")
    public RuleServiceResponse deleteRule(RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.delete(ruleServiceRequest);
    }
}
