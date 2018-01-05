package com.services.micro.rules.search.bl.repository;

import com.services.micro.rules.search.api.RuleEntity;
import com.services.micro.rules.search.api.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RuleRepository extends MongoRepository<RuleEntity, String> {
    RuleEntity findByPackageNameAndRuleName(String packageName, String ruleName);

    List<RuleEntity> findByStatus(Status status);

    List<RuleEntity> findByPackageNameLikeAndRuleNameLike(
            String packageName,
            String ruleName);
}
