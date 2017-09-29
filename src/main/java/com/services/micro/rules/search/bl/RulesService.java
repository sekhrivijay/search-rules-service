package com.services.micro.rules.search.bl;

import com.services.micro.rules.search.api.request.RuleServiceRequest;
import com.services.micro.rules.search.api.response.RuleServiceResponse;

public interface RulesService {
//    RuleServiceResponse getResponse(String key);
//    RuleServiceResponse getResponse();

    RuleServiceResponse create(RuleServiceRequest ruleServiceRequest) throws Exception;
    RuleServiceResponse read(RuleServiceRequest ruleServiceRequest) throws Exception;
    RuleServiceResponse update(RuleServiceRequest ruleServiceRequest) throws Exception;
    RuleServiceResponse delete(RuleServiceRequest ruleServiceRequest) throws Exception;
}
