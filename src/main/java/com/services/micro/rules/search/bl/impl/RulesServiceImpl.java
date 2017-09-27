package com.services.micro.rules.search.bl.impl;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.services.micro.commons.logging.annotation.LogExecutionTime;
import com.services.micro.rules.search.api.response.ServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import com.services.micro.rules.search.config.RulesConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service(value = "RulesService")
@EnableConfigurationProperties(RulesConfigurationProperties.class)
public class RulesServiceImpl implements RulesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesServiceImpl.class);


    @Autowired
    private RulesConfigurationProperties rulesConfigurationProperties;

    @Value("${service.myKey1}")
    private String myKey1;

    @Override
    @Timed
    @ExceptionMetered
    @HystrixCommand(groupKey = "hystrixGroup",
            commandKey = "helloCommandKey",
            threadPoolKey = "helloThreadPoolKey",
            fallbackMethod = "fallbackHello")
    @Cacheable(cacheNames = "default")
    @LogExecutionTime
    public ServiceResponse getResponse(String key) {
        LOGGER.info("getResponse called ");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("Hello " + key + rulesConfigurationProperties.getMyKey1() + "  key1 is " + myKey1);
        serviceResponse.setType("valid");
        return serviceResponse;
    }

    public ServiceResponse fallbackHello(String name) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("This is Hello fromm fallback " + name);
        return serviceResponse;
    }


}
