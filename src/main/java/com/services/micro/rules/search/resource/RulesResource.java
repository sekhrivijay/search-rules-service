package com.services.micro.rules.search.resource;

import com.services.micro.rules.search.api.request.RuleServiceRequest;
import com.services.micro.rules.search.api.response.RuleServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequestMapping("/api/v1/rules")
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

    @PutMapping("/")
    public RuleServiceResponse updateRule(@RequestBody RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.update(ruleServiceRequest);
    }

    @GetMapping("/")
    public RuleServiceResponse readRule(RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.read(ruleServiceRequest);
    }

    @DeleteMapping("/")
    public RuleServiceResponse deleteRule(RuleServiceRequest ruleServiceRequest) throws Exception {
        return rulesService.delete(ruleServiceRequest);
    }
}
