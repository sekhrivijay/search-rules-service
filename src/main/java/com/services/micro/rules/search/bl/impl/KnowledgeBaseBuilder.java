package com.services.micro.rules.search.bl.impl;


import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;

import org.drools.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBaseBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeBaseBuilder.class);
    private static KnowledgeBase readKnowledgeBase() throws Exception {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add(ResourceFactory.newClassPathResource("Pune.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newClassPathResource("Nagpur.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                LOGGER.error(error.toString());
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        return kbase;
    }
}
