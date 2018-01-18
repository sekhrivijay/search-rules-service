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
import com.ftd.services.rules.search.bl.repository.RuleReloadTriggerEntity;
import com.ftd.services.rules.search.bl.repository.RuleReloadTriggerRepository;
import com.ftd.services.rules.search.bl.repository.RuleRepository;
import com.ftd.services.rules.search.config.AppConfig;
import com.ftd.services.rules.search.config.RulesConfiguration;
import com.ftd.services.search.config.GlobalConstants;

/**
 * When an update (adds too) arrives we update the database (mongodb). We also
 * update the trigger collection on mongodb. What we don't do is actually update
 * the rule knowledgebase. This will happen on a scheduled basis rather than
 * real-time.
 *
 */
@Service(value = "RulesService")
@EnableConfigurationProperties(AppConfig.class)
public class RulesServiceImpl implements RulesService {

    private static final Logger         LOGGER           = LoggerFactory.getLogger(RulesServiceImpl.class);

    private RulesConfiguration          rulesConfiguration;
    private RuleRepository              ruleRepository;
    private RuleReloadTriggerRepository ruleReloadTriggerRepository;
    private AppConfig                   appConfig;

    /*
     * This is a local variable that remains valid while this instance is running.
     * It will be reinitialized from the database the next time the process starts.
     */
    private long                        lastReloadTimeMS = 0;

    public RulesServiceImpl(
            @Autowired RulesConfiguration rulesConfiguration,
            @Autowired AppConfig appConfig,
            @Autowired RuleRepository ruleRepository,
            @Autowired RuleReloadTriggerRepository ruleReloadTriggerRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleReloadTriggerRepository = ruleReloadTriggerRepository;
        this.rulesConfiguration = rulesConfiguration;
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void prepareRuleKnowledgebaseAtStartup() {
        try {
            /*
             * Just calling the method to load the initial trigger timestamp. This will keep
             * us from unnecessarily reloading the rules database after the first trigger
             * time delay.
             */
            isRuleDatabaseModified();
            /*
             * We need to load the rules regardless of what the trigger indicates.
             */
            reloadRules();
        } catch (Exception e) {
            LOGGER.warn("initial load of the KB", e);
        }
    }

    /**
     * This method will wake up a timed interval. All it does is read the trigger
     * document and exit if nothing has changed. Otherwise it will go ahead and
     * reload the rules. A side-effect of checking the trigger is that the current
     * trigger timestamp is saved for the next comparison.
     */
    @Scheduled(fixedRateString = "${service.ruleReloadRate}")
    public void scheduledRefresh() {
        try {
            if (isRuleDatabaseModified()) {
                reloadRules();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity create(RuleEntity ruleEntityFromClient) throws Exception {
        validateServiceRequest(ruleEntityFromClient);
        RuleEntity ruleEntityFromDB = getRuleEntityFromDB(ruleEntityFromClient);
        if (ruleEntityFromDB != null) {
            throw new Exception(
                    "A rule with same ruleName, packageName, serviceName, and environment already exist");
        }
        RuleEntity savedRule = ruleRepository.save(ruleEntityFromClient);
        updateTimestampOnTrigger();
        return savedRule;
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
        RuleEntity ruleEntityFromDB = ruleRepository.findOne(id);
        if (ruleEntityFromDB == null) {
            throw new Exception("rule id not found in database on update: " + id);
        }
        ruleEntityFromClient.setId(ruleEntityFromDB.getId());
        RuleEntity savedRule = ruleRepository.save(ruleEntityFromClient);
        updateTimestampOnTrigger();
        return savedRule;
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleEntity delete(String id) throws Exception {
        RuleEntity ruleEntityFromDB = ruleRepository.findOne(id);
        if (ruleEntityFromDB == null) {
            throw new Exception("id not found for delete: " + id);
        }
        ruleRepository.delete(id);
        updateTimestampOnTrigger();
        return ruleEntityFromDB;
    }

    /**
     * This method detects changes in the rule database by querying the trigger
     * document. If the timestamp on the trigger is greater than it was the last
     * time the trigger was queried then the rules have changed and must be
     * reloaded.
     *
     * @return
     */
    boolean isRuleDatabaseModified() {

        RuleReloadTriggerEntity trigger = ruleReloadTriggerRepository.findById(RuleReloadTriggerRepository.TRIGGER_KEY);

        if (trigger == null) {
            return false;
        }
        if (trigger.getLastUpdatedMS().longValue() > lastReloadTimeMS) {
            lastReloadTimeMS = trigger.getLastUpdatedMS().longValue();
            return true;
        }
        return false;
    }

    /**
     * This method must be called each time a rule is modified (or deleted) in the
     * database. This is a trigger for all processes that care to reload the rules
     * when they have changed. The trigger is a simple timestamp. When a process
     * finds a timestamp on this document that is greater than their locally stored
     * timestamp it is the indication the rules have changed since they were last
     * loaded.
     */
    void updateTimestampOnTrigger() {

        RuleReloadTriggerEntity trigger = ruleReloadTriggerRepository.findById(RuleReloadTriggerRepository.TRIGGER_KEY);

        if (trigger == null) {
            trigger = new RuleReloadTriggerEntity();
            trigger.setId(RuleReloadTriggerRepository.TRIGGER_KEY);
            trigger.setLastUpdatedMS(System.currentTimeMillis());
            ruleReloadTriggerRepository.insert(trigger);
            return;
        }
        trigger.setLastUpdatedMS(System.currentTimeMillis());
        ruleReloadTriggerRepository.save(trigger);
    }

    private RuleEntity getRuleEntityFromDB(RuleEntity ruleEntity) {
        return ruleRepository.findByPackageNameAndRuleName(
                ruleEntity.getPackageName(),
                ruleEntity.getRuleName());
    }

    private void validateServiceRequest(RuleEntity ruleServiceRequest) throws Exception {
        LOGGER.info(ruleServiceRequest.toString());
        if (StringUtils.isEmpty(ruleServiceRequest.getRuleName())
                || StringUtils.isEmpty(ruleServiceRequest.getPackageName())) {
            throw new Exception(
                    "Missing mandatory fields. " +
                            "Both fields ruleName and packageName are required");
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
                .forEach(status -> ruleRepository.findByStatus(Status.getStatus(status))
                        .forEach(ruleEntity -> createOrUpdateRuleInKBase(
                                getResource(ruleEntity),
                                knowledgeBuilderTmp,
                                kbaseTmp)));
        LOGGER.info("knowledgebase loaded");
        rulesConfiguration.setKbase(kbaseTmp);
        rulesConfiguration.setKbuilder(knowledgeBuilderTmp);
    }

    private void createOrUpdateRuleInKBase(Resource resource, KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        kbuilder.add(resource, ResourceType.DRL);
        kbase.addPackages(kbuilder.getKnowledgePackages());
    }

}
