package com.ftd.services.rules.search.bl;

import java.util.List;

import com.ftd.services.rules.search.api.RuleEntity;

public interface RulesService {
    RuleEntity create(RuleEntity ruleEntity) throws Exception;

    List<RuleEntity> read(RuleEntity ruleEntity) throws Exception;

    List<RuleEntity> read() throws Exception;

    RuleEntity readById(String id) throws Exception;

    RuleEntity update(String id, RuleEntity ruleEntity) throws Exception;

    RuleEntity delete(String id) throws Exception;

    void reloadKb() throws Exception;
}
