package com.services.micro.rules.search.resource;

import com.services.micro.commons.logging.annotation.LogExecutionTime;
import com.services.micro.rules.search.api.request.RuleServiceRequest;
import com.services.micro.rules.search.api.response.RuleServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@RestController
//@RefreshScope
//@RequestMapping("/test")
public class ServiceResource {

//    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResource.class);
//
//    @Autowired
//    private RulesService rulesService;
//
//    @GetMapping
//    public RuleServiceResponse getMessage(String a) {
//        LOGGER.info("getMessage called");
//        return rulesService.getResponse(a);
//    }
//
//    @GetMapping(value = "/change")
//    public RuleServiceResponse getMess() {
//        LOGGER.info("getMess called");
//        return rulesService.getResponse();
//    }
//
//    @GetMapping(value = "/plain")
//    @LogExecutionTime
//    public String helloWorlda() {
//        return rulesService.getResponse("test").getMessage();
//    }
//
//    @GetMapping(value = "/hello")
//    @LogExecutionTime
//    public String getHello(String key1, String key2) {
//        return "hello " + key1 + "  " + key2;
//    }
//
//    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @LogExecutionTime
//    public RuleServiceResponse post(@RequestBody RuleServiceRequest ruleServiceRequest) {
//        RuleServiceResponse ruleServiceResponse = new RuleServiceResponse();
////        ruleServiceResponse.setMessage("Hello " + ruleServiceRequest.getInput());
//        ruleServiceResponse.setType("post");
//        return ruleServiceResponse;
//    }

}


