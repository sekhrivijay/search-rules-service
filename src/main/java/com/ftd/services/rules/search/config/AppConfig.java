package com.ftd.services.rules.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "service")
@EnableConfigurationProperties
@Component
public class AppConfig {
    private List<String> ruleStatusList;

    public AppConfig() {
        this.ruleStatusList = new ArrayList<>();
    }

    public List<String> getRuleStatusList() {
        return ruleStatusList;
    }

    public void setRuleStatusList(List<String> ruleStatusList) {
        this.ruleStatusList = ruleStatusList;
    }
}
