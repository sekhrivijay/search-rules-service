package com.ftd.services.rules.search.bl.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ftd.services.rules.search.api.RuleEntity;
import com.ftd.services.rules.search.api.Status;
import com.ftd.services.rules.search.bl.RulesService;
import com.ftd.services.rules.search.bl.repository.RuleRepository;
import com.ftd.services.rules.search.config.AppConfig;
import com.ftd.services.rules.search.config.RulesConfiguration;
import com.ftd.services.search.config.GlobalConstants;

@Service(value = "RulesService")
@EnableConfigurationProperties(AppConfig.class)
public class RulesServiceImpl implements RulesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesServiceImpl.class);

    private RulesConfiguration  rulesConfiguration;

    private RuleRepository      ruleRepository;

    private AppConfig           appConfig;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Autowired
    public void setRulesConfiguration(RulesConfiguration rulesConfiguration) {
        this.rulesConfiguration = rulesConfiguration;
    }

    @PostConstruct
    public void loadRules() {
        appConfig.getRuleStatusList()
                .forEach(status -> ruleRepository
                        .findByStatus(Status.getStatus(status))
                        .forEach(this::createOrUpdateRuleInKBase));
    }

    /*
     * What is scheduled here will change to making it conditional on a "flag".
     */
    @Scheduled(fixedRateString = "${service.ruleReloadRate}")
    public void scheduledRefresh() {
        LOGGER.info("Reloading all rules ..");
        try {
            reloadRules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh(RuleEntity ruleEntity, KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        createOrUpdateRuleInKBase(ruleEntity, kbuilder, kbase);
    }

    @Autowired
    public void setRuleRepository(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity create(RuleEntity ruleEntityFromClient) throws Exception {
        validateServiceRequest(ruleEntityFromClient);
        RuleEntity ruleEntityFromDB = getRuleEntityFromDB(ruleEntityFromClient);
        if (ruleEntityFromDB != null) {
            throw new Exception("A rule with same ruleName , packageName, serviceName and environment already exist");
        }

        createOrUpdateRuleInKBase(ruleEntityFromClient);
        return ruleRepository.save(ruleEntityFromClient);
    }

    private Resource getResource(RuleEntity ruleEntity) {
        return ResourceFactory.newByteArrayResource(ruleEntity.getRule().getBytes());
    }

    @Override
    @Timed
    @ExceptionMetered
    public List<RuleEntity> read() throws Exception {
        return ruleRepository.findAll();
    }

    @Override
    @Timed
    @ExceptionMetered
    public List<RuleEntity> read(RuleEntity ruleEntityFromClient) throws Exception {
        return ruleRepository.findByPackageNameLikeAndRuleNameLike(
                StringUtils.defaultString(ruleEntityFromClient.getPackageName(), GlobalConstants.STAR),
                StringUtils.defaultString(ruleEntityFromClient.getRuleName(), GlobalConstants.STAR));
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity readById(String id) throws Exception {
        RuleEntity ruleEntityFromDB = ruleRepository.findOne(id);
        if (ruleEntityFromDB == null) {
            return null;
        }
        return ruleEntityFromDB;
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity update(String id, RuleEntity ruleEntityFromClient) throws Exception {
        RuleEntity ruleEntityFromDB = delete(id);
        if (ruleEntityFromDB == null) {
            return null;
        }
        return create(ruleEntityFromClient);
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity delete(String id) throws Exception {
        RuleEntity ruleEntityFromDB = readById(id);
        if (ruleEntityFromDB == null) {
            return null;
        }

        ruleRepository.delete(id);
        rulesConfiguration.getKbase().removeRule(ruleEntityFromDB.getPackageName(), ruleEntityFromDB.getRuleName());
        return ruleEntityFromDB;
    }

    private RuleEntity getRuleEntityFromDB(RuleEntity ruleEntity) {
        return ruleRepository.findByPackageNameAndRuleName(
                ruleEntity.getPackageName(),
                ruleEntity.getRuleName());
    }

    private void createOrUpdateRuleInKBase(RuleEntity ruleEntity) {
        createOrUpdateRuleInKBase(ruleEntity, rulesConfiguration.getKbuilder(), rulesConfiguration.getKbase());
    }

    private void createOrUpdateRuleInKBase(RuleEntity ruleEntity,
            KnowledgeBuilder kbuilder,
            InternalKnowledgeBase kbase) {
        LOGGER.info("Adding rule into knowledge base " + ruleEntity);
        createOrUpdateRuleInKBase(getResource(ruleEntity), kbuilder, kbase);
    }

    private void createOrUpdateRuleInKBase(Resource resource, KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        kbuilder.add(resource, ResourceType.DRL);
        kbase.addPackages(kbuilder.getKnowledgePackages());
    }

    private void validateServiceRequest(RuleEntity ruleServiceRequest) throws Exception {
        LOGGER.info(ruleServiceRequest.toString());
        if (StringUtils.isEmpty(ruleServiceRequest.getRuleName())
                || StringUtils.isEmpty(ruleServiceRequest.getPackageName())) {
            throw new Exception("Missing mandatory fields. " +
                    "Both 2 fields ruleName and packageName are required");
        }
    }

    @Override
    public void reloadRuleKb() throws Exception {
        reloadRules();
    }

    void reloadRules() throws Exception {
        InternalKnowledgeBase kbaseTmp = rulesConfiguration.createNewInternalKnowledgeBase();
        KnowledgeBuilder knowledgeBuilderTmp = rulesConfiguration.createNewKnowledgeBuilder();

        appConfig.getRuleStatusList()
                .forEach(status -> ruleRepository
                        .findByStatus(Status.getStatus(status))
                        .forEach(ruleEntity -> refresh(ruleEntity, knowledgeBuilderTmp, kbaseTmp)));
        rulesConfiguration.setKbase(kbaseTmp);
        rulesConfiguration.setKbuilder(knowledgeBuilderTmp);
    }
}
