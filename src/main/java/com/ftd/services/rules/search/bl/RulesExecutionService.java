package com.ftd.services.rules.search.bl;

import com.ftd.services.search.bl.clients.rules.RuleServiceResponse;

public interface RulesExecutionService {
    RuleServiceResponse executePre(RuleServiceResponse response) throws Exception;
    RuleServiceResponse executePost(RuleServiceResponse response) throws Exception;
}
