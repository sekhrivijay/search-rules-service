package com.ftd.services.rules.search.controller;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ftd.services.rules.search.api.RuleEntity;
import com.ftd.services.rules.search.bl.RulesService;

/**
 * This class provides a restful API to maintain the drools rules. The most
 * interesting thing to note is that the rule attribute in the RuleEntity is
 * encoded to base64 during transit. This is because JSON does not handle the
 * drools rule formats very well and this was the "workaround".
 *
 */
@RestController
@RefreshScope
@RequestMapping("/api/rules")
public class RulesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesController.class);

    private RulesService        rulesService;

    public RulesController(@Autowired RulesService rulesService) {
        this.rulesService = rulesService;
    }

    void decodeRuleFromTransport(RuleEntity ruleEntity) {
        if (ruleEntity == null || ruleEntity.getRule() == null) {
            return;
        }
        try {
            ruleEntity.setRule(new String(Base64.getDecoder().decode(ruleEntity.getRule())));
        } catch (Exception e) {
            LOGGER.warn("base64 encoding error on request", e.getMessage());
            return;
        }
    }

    void encodeRuleForTransport(RuleEntity ruleEntity) {
        if (ruleEntity == null || ruleEntity.getRule() == null) {
            return;
        }
        try {
            ruleEntity.setRule(new String(Base64.getEncoder().encode(ruleEntity.getRule().getBytes())));
        } catch (Exception e) {
            LOGGER.warn("base64 encoding error on response", e.getMessage());
            return;
        }
    }

    @PostMapping("/")
    public ResponseEntity<RuleEntity> createRule(@RequestBody RuleEntity ruleEntity) throws Exception {
        decodeRuleFromTransport(ruleEntity);
        return buildResponse(rulesService.create(ruleEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleEntity> updateRule(
            @PathVariable String id,
            @RequestBody RuleEntity ruleEntity) throws Exception {
        decodeRuleFromTransport(ruleEntity);
        return buildResponse(rulesService.update(id, ruleEntity));
    }

    @GetMapping("/reloadKb")
    public void reloadRuleKb() throws Exception {
        rulesService.reloadKb();
    }

    @GetMapping("/query")
    public List<RuleEntity> getRulesBy(RuleEntity ruleEntity) throws Exception {
        decodeRuleFromTransport(ruleEntity);
        List<RuleEntity> ruleList = rulesService.read(ruleEntity);
        ruleList.stream().forEach(this::encodeRuleForTransport);
        return ruleList;
    }

    @GetMapping("/")
    public List<RuleEntity> getAllRules() throws Exception {
        List<RuleEntity> ruleList = rulesService.read();
        ruleList.stream().forEach(this::encodeRuleForTransport);
        return ruleList;
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
        encodeRuleForTransport(ruleEntity);
        return ResponseEntity.ok().body(ruleEntity);
    }
}
