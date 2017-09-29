package com.services.micro.rules.search.api.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleServiceRequest implements Serializable  {
    private String id;
    private String environment;
    private String serviceName;
    private String ruleName;
    private String packageName;
    private String rule;
    private Map<String, String> metaData = new HashMap<>();

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RuleServiceRequest{" +
                "environment='" + environment + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", rule='" + rule + '\'' +
                ", metaData=" + metaData +
                '}';
    }


    public static final class RuleServiceRequestBuilder {
        private String environment;
        private String serviceName;
        private String ruleName;
        private String packageName;
        private String rule;
        private Map<String, String> metaData = new HashMap<>();

        private RuleServiceRequestBuilder() {
        }

        public static RuleServiceRequestBuilder aRuleServiceRequest() {
            return new RuleServiceRequestBuilder();
        }

        public RuleServiceRequestBuilder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public RuleServiceRequestBuilder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RuleServiceRequestBuilder withRuleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public RuleServiceRequestBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public RuleServiceRequestBuilder withRule(String rule) {
            this.rule = rule;
            return this;
        }

        public RuleServiceRequestBuilder withMetaData(Map<String, String> metaData) {
            this.metaData = metaData;
            return this;
        }

        public RuleServiceRequest build() {
            RuleServiceRequest ruleServiceRequest = new RuleServiceRequest();
            ruleServiceRequest.setEnvironment(environment);
            ruleServiceRequest.setServiceName(serviceName);
            ruleServiceRequest.setRuleName(ruleName);
            ruleServiceRequest.setPackageName(packageName);
            ruleServiceRequest.setRule(rule);
            ruleServiceRequest.setMetaData(metaData);
            return ruleServiceRequest;
        }
    }
}
