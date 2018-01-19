package com.ftd.services.rules.search.bl;

import java.util.List;

import com.ftd.services.rules.search.api.RuleEntity;

public interface RulesService {
    RuleEntity create(RuleEntity ruleEntity);

    List<RuleEntity> read(RuleEntity ruleEntity);

    List<RuleEntity> read();

    RuleEntity readById(String id);

    RuleEntity update(String id, RuleEntity ruleEntity);

    RuleEntity delete(String id);

    void reloadKb();
}
