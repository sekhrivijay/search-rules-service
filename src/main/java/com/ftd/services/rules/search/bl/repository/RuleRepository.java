package com.ftd.services.rules.search.bl.repository;

import com.ftd.services.rules.search.api.Status;
import com.ftd.services.rules.search.api.RuleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RuleRepository extends MongoRepository<RuleEntity, String> {
    RuleEntity findByPackageNameAndRuleName(String packageName, String ruleName);

    List<RuleEntity> findByStatus(Status status);

    List<RuleEntity> findByPackageNameLikeAndRuleNameLike(
            String packageName,
            String ruleName);
}
