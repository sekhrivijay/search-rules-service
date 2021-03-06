package com.ftd.services.rules.search.config;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesConfiguration {

    private InternalKnowledgeBase kbase;
    private KnowledgeBuilder kbuilder;

    @Bean
    public InternalKnowledgeBase kbase() {
        return createNewInternalKnowledgeBase();
    }

    @Bean
    public KnowledgeBuilder kbuilder() {
        return createNewKnowledgeBuilder();
    }

    public InternalKnowledgeBase getKbase() {
        return kbase;
    }

    @Autowired
    public void setKbase(InternalKnowledgeBase kbase) {
        this.kbase = kbase;
    }

    public KnowledgeBuilder getKbuilder() {
        return kbuilder;
    }

    @Autowired
    public void setKbuilder(KnowledgeBuilder kbuilder) {
        this.kbuilder = kbuilder;
    }

    public InternalKnowledgeBase createNewInternalKnowledgeBase() {
        return KnowledgeBaseFactory.newKnowledgeBase();
    }

    public KnowledgeBuilder createNewKnowledgeBuilder() {
        return KnowledgeBuilderFactory.newKnowledgeBuilder();
    }
}
