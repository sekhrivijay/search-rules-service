package com.services.micro.rules.search.resource;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RefreshScope
@RequestMapping("/rules")
public class RulesExecuationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesExecuationResource.class);


    @GetMapping("/executePre")
    public SearchServiceRequest executePre(SearchServiceRequest searchServiceRequest) {
        return null;
    }

    @GetMapping("/executePost")
    public SearchServiceResponse executePost(SearchServiceRequest searchServiceRequest, SearchServiceResponse searchServiceResponse) {
        return null;
    }
}


