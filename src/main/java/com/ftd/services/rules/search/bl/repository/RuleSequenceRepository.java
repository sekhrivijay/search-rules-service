package com.ftd.services.rules.search.bl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleSequenceRepository extends MongoRepository<RuleSequenceEntity, String> {

    RuleSequenceEntity findById(String id);

}
