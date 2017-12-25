package com.services.micro.rules.search.api.request;

import com.services.micro.rules.search.api.Status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleServiceRequest implements Serializable  {
    private String id;
    private String environment;
    private String serviceName;
    private String ruleName;
    private String packageName;
    private Status status = Status.DEFAULT;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RuleServiceRequest{" +
                "environment='" + environment + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", rule='" + rule + '\'' +
                ", status='" + status + '\'' +
                ", metaData=" + metaData +
                '}';
    }

}
