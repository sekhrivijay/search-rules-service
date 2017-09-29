package com.services.micro.rules.search.api.response;

import com.services.micro.rules.search.api.request.RuleServiceRequest;
import org.kie.api.definition.rule.Rule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleServiceResponse implements Serializable {
    private String environment;
    private String serviceName;
    private String ruleName;
    private String packageName;
    private String rule;
    private String session;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }


    public static final class RuleServiceResponseBuilder {
        private String environment;
        private String serviceName;
        private String ruleName;
        private String packageName;
        private String rule;
        private String session;
        private Map<String, String> metaData = new HashMap<>();

        private RuleServiceResponseBuilder() {
        }

        public static RuleServiceResponseBuilder aRuleServiceResponse() {
            return new RuleServiceResponseBuilder();
        }

        public RuleServiceResponseBuilder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public RuleServiceResponseBuilder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RuleServiceResponseBuilder withRuleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public RuleServiceResponseBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public RuleServiceResponseBuilder withRule(String rule) {
            this.rule = rule;
            return this;
        }

        public RuleServiceResponseBuilder withSession(String session) {
            this.session = session;
            return this;
        }

        public RuleServiceResponseBuilder withMetaData(Map<String, String> metaData) {
            this.metaData = metaData;
            return this;
        }

        public RuleServiceResponse build() {
            RuleServiceResponse ruleServiceResponse = new RuleServiceResponse();
            ruleServiceResponse.setEnvironment(environment);
            ruleServiceResponse.setServiceName(serviceName);
            ruleServiceResponse.setRuleName(ruleName);
            ruleServiceResponse.setPackageName(packageName);
            ruleServiceResponse.setRule(rule);
            ruleServiceResponse.setSession(session);
            ruleServiceResponse.setMetaData(metaData);
            return ruleServiceResponse;
        }
    }
}
