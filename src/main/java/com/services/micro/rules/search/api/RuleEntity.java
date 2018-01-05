package com.services.micro.rules.search.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleEntity implements Serializable {
    private String id;
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
                ", ruleName='" + ruleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", status=" + status +
                ", rule='" + rule + '\'' +
                ", metaData=" + metaData +
                '}';
    }

}
