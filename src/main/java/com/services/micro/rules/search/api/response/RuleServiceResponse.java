package com.services.micro.rules.search.api.response;

import org.kie.api.definition.rule.Rule;

import java.io.Serializable;

public class RuleServiceResponse implements Serializable {
    private String message;
    private String type;
    private Rule rule;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
