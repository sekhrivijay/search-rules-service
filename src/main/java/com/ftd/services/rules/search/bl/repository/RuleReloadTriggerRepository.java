package com.ftd.services.rules.search.bl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleReloadTriggerRepository extends MongoRepository<RuleReloadTriggerEntity, String> {

    String TRIGGER_KEY = "RELOAD_TRIGGER";

    RuleReloadTriggerEntity findById(String id);
}
