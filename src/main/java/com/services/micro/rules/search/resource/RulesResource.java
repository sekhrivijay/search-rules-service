package com.services.micro.rules.search.resource;

import com.google.gson.Gson;
import com.services.micro.rules.search.api.RuleEntity;
import com.services.micro.rules.search.bl.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/api/rules")
public class RulesResource {

    private RulesService rulesService;

    @Autowired
    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping("/")
    public RuleEntity createRule(@RequestBody RuleEntity ruleEntity) throws Exception {
        System.out.println("request .... " + new Gson().toJson(ruleEntity));
        return rulesService.create(ruleEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleEntity> updateRule(@PathVariable String id,
                                                 @RequestBody RuleEntity ruleEntity) throws Exception {
        return buildResponse(rulesService.update(id, ruleEntity));
    }

    @GetMapping("/query")
    public List<RuleEntity> getRulesBy(RuleEntity ruleEntity) throws Exception {
        return rulesService.read(ruleEntity);
    }

    @GetMapping("/")
    public List<RuleEntity> getAllRules() throws Exception {
        return rulesService.read();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuleEntity> getRuleById(@PathVariable String id) throws Exception {
        return buildResponse(rulesService.readById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RuleEntity> deleteRule(@PathVariable String id) throws Exception {
        return buildResponse(rulesService.delete(id));
    }

    private ResponseEntity<RuleEntity> buildResponse(RuleEntity ruleEntity) {
        if (ruleEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(ruleEntity);
    }
}
