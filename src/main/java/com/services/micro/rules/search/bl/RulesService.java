package com.services.micro.rules.search.bl;

import com.services.micro.rules.search.api.response.ServiceResponse;

public interface RulesService {
    ServiceResponse getResponse(String key);
    ServiceResponse getResponse();
}
