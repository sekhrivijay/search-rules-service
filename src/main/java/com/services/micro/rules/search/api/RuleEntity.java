package com.services.micro.rules.search.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleEntity implements Serializable {
    private String id;
    private String environment;
    private String serviceName;
    private String ruleName;
    private String packageName;
    private Status status = Status.INACTIVE;
    private String rule;
    private Map<String, String> metaData = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "RuleEntity{" +
                "id='" + id + '\'' +
                ", environment='" + environment + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", status=" + status +
                ", rule='" + rule + '\'' +
                ", metaData=" + metaData +
                '}';
    }

    public static final class RuleEntityBuilder {
        private String id;
        private String environment;
        private String serviceName;
        private String ruleName;
        private String packageName;
        private Status status = Status.INACTIVE;
        private String rule;
        private Map<String, String> metaData = new HashMap<>();

        private RuleEntityBuilder() {
        }

        public static RuleEntityBuilder aRuleEntity() {
            return new RuleEntityBuilder();
        }

        public RuleEntityBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public RuleEntityBuilder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public RuleEntityBuilder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RuleEntityBuilder withRuleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public RuleEntityBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public RuleEntityBuilder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public RuleEntityBuilder withRule(String rule) {
            this.rule = rule;
            return this;
        }

        public RuleEntityBuilder withMetaData(Map<String, String> metaData) {
            this.metaData = metaData;
            return this;
        }

        public RuleEntity build() {
            RuleEntity ruleEntity = new RuleEntity();
            ruleEntity.setId(id);
            ruleEntity.setEnvironment(environment);
            ruleEntity.setServiceName(serviceName);
            ruleEntity.setRuleName(ruleName);
            ruleEntity.setPackageName(packageName);
            ruleEntity.setStatus(status);
            ruleEntity.setRule(rule);
            ruleEntity.setMetaData(metaData);
            return ruleEntity;
        }
    }
}
