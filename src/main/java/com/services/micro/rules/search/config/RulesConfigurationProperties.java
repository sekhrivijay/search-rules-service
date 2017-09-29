package com.services.micro.rules.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "service.rules")
@EnableConfigurationProperties
@Component
public class RulesConfigurationProperties {

}
