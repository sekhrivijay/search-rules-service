package com.services.micro.rules.search.bl.repository;

import com.services.micro.rules.search.api.request.RuleServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository extends MongoRepository<RuleServiceRequest, String> {
    RuleServiceRequest findByPackageNameAndRuleName(String packageName, String ruleName);
    RuleServiceRequest findByPackageNameAndRuleNameAndServiceNameAndEnvironment(String packageName, String ruleName, String serviceName, String environment);
}
