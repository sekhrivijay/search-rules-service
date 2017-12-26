package com.services.micro.rules.search.bl;

import com.services.micro.rules.search.api.RuleEntity;

import java.util.List;

public interface RulesService {
    RuleEntity create(RuleEntity ruleEntity) throws Exception;

    List<RuleEntity> read(RuleEntity ruleEntity) throws Exception;

    List<RuleEntity> read() throws Exception;

    RuleEntity readById(String id) throws Exception;

    RuleEntity update(String id, RuleEntity ruleEntity) throws Exception;

    RuleEntity delete(String id) throws Exception;
}
