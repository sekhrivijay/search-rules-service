package com.services.micro.rules.search.config;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesConfiguration {
    @Bean
    public InternalKnowledgeBase kbase() {
        return KnowledgeBaseFactory.newKnowledgeBase();
    }

    @Bean
    public KnowledgeBuilder kbuilder(){
        return KnowledgeBuilderFactory.newKnowledgeBuilder();
    }

}
