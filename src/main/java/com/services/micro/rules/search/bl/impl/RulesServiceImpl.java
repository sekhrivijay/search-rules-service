package com.services.micro.rules.search.bl.impl;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.services.micro.commons.logging.annotation.LogExecutionTime;
import com.services.micro.rules.search.api.request.RuleServiceRequest;
import com.services.micro.rules.search.api.response.RuleServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import com.services.micro.rules.search.bl.repository.RuleRepository;
import com.services.micro.rules.search.config.RulesConfiguration;
import com.services.micro.rules.search.config.RulesConfigurationProperties;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Service(value = "RulesService")
@EnableConfigurationProperties(RulesConfigurationProperties.class)
public class RulesServiceImpl implements RulesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesServiceImpl.class);

//    private InternalKnowledgeBase kbase;
//    private KnowledgeBuilder kbuilder;
    private RulesConfiguration rulesConfiguration;

    @Autowired
    public void setRulesConfiguration(RulesConfiguration rulesConfiguration) {
        this.rulesConfiguration = rulesConfiguration;
    }
//    @Autowired
//    public void setKbase(InternalKnowledgeBase kbase) {
//        this.kbase = kbase;
//    }
//    @Autowired
//    public void setKbuilder(KnowledgeBuilder kbuilder) {
//        this.kbuilder = kbuilder;
//    }

    private RuleRepository ruleRepository;

    @PostConstruct
    public void loadRules() {
//        rulesConfiguration.setKbase();
        ruleRepository
                .findAll()
                .forEach(this::createOrUpdateRule);
    }

    @Scheduled(fixedRate = 50000)
    public void refreshRules() {
        InternalKnowledgeBase kbaseTmp = rulesConfiguration.kbase();
        KnowledgeBuilder knowledgeBuilderTmp = rulesConfiguration.kbuilder();
        ruleRepository
                .findAll()
                .forEach(e -> refresh(e, knowledgeBuilderTmp, kbaseTmp));
        rulesConfiguration.setKbase(kbaseTmp);
        rulesConfiguration.setKbuilder(knowledgeBuilderTmp);
    }

    private void refresh(RuleServiceRequest ruleServiceRequest,  KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        createOrUpdateRule(ruleServiceRequest, kbuilder, kbase);
    }


    @Autowired
    public void setRuleRepository(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse create(RuleServiceRequest ruleServiceRequest) throws Exception {
        validateServiceRequest(ruleServiceRequest);
        RuleServiceRequest ruleServiceRequestFromDb = getRuleServiceRequestFromDb(ruleServiceRequest);
        if(ruleServiceRequestFromDb != null) {
            throw new Exception("A rule with same ruleName , packageName, serviceName and environment already exist");
        }
        createOrUpdateRule(ruleServiceRequest);
        ruleRepository.save(ruleServiceRequest);
        return buildRuleServiceResponse(ruleServiceRequest, getRuleServiceRequestFromDb(ruleServiceRequest));
    }

    private Resource getResource(RuleServiceRequest ruleServiceRequest) {
        return ResourceFactory.newByteArrayResource(ruleServiceRequest.getRule().getBytes());
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse read(RuleServiceRequest ruleServiceRequest) throws Exception {
        validateServiceRequest(ruleServiceRequest);
        RuleServiceRequest ruleServiceRequestFromDb = getRuleServiceRequestFromDb(ruleServiceRequest);
        Rule rule = rulesConfiguration.getKbase().getRule(ruleServiceRequest.getPackageName(), ruleServiceRequest.getRuleName());
        if(ruleServiceRequestFromDb == null || rule == null) {
            throw new Exception("Rule could not be found");
        }

        return buildRuleServiceResponse(ruleServiceRequest, ruleServiceRequestFromDb);
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse update(RuleServiceRequest ruleServiceRequest) throws Exception {
        delete(ruleServiceRequest);
        return create(ruleServiceRequest);
    }

    @Override
    @Timed
    @ExceptionMetered
    public RuleServiceResponse delete(RuleServiceRequest ruleServiceRequest) throws Exception {
        validateServiceRequest(ruleServiceRequest);

        rulesConfiguration.getKbase().removeRule(ruleServiceRequest.getPackageName(), ruleServiceRequest.getRuleName());
        ruleRepository.delete(getRuleServiceRequestFromDb(ruleServiceRequest));
        return null;
    }


    public void executePost(String key) {
        KieSession knowledgeSession = null;
        try {

            knowledgeSession = rulesConfiguration.getKbase().newKieSession();

            // 4 - create and assert some facts
//            Person rocky = new Person("Rocky Balboa", "Philadelphia", 35);

//            knowledgeSession.insert(rocky);
            final Message message = new Message();
            message.setMessage( "Hello World" );
            if (key.equals("test1")) {
                message.setStatus(Message.HELLO);
            }else {
                message.setStatus(Message.GOODBYE);

            }

            knowledgeSession.insert( message );
//            knowledgeSession.insert("vijay");

            // 5 - fire the rules
            knowledgeSession.fireAllRules();

            System.out.println(message);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            knowledgeSession.dispose();
        }
    }



    private RuleServiceResponse buildRuleServiceResponse(RuleServiceRequest ruleServiceRequest, RuleServiceRequest ruleServiceRequestFromDb) {
        return RuleServiceResponse.RuleServiceResponseBuilder.aRuleServiceResponse()
                    .withEnvironment(ruleServiceRequest.getEnvironment())
                    .withMetaData(ruleServiceRequest.getMetaData())
                    .withServiceName(ruleServiceRequest.getServiceName())
                    .withPackageName(ruleServiceRequest.getPackageName())
                    .withRuleName(ruleServiceRequest.getRuleName())
                    .withRule(ruleServiceRequestFromDb.getRule())
                    .build();
    }

    private RuleServiceRequest getRuleServiceRequestFromDb(RuleServiceRequest ruleServiceRequest) {
        return ruleRepository.findByPackageNameAndRuleNameAndServiceNameAndEnvironment(
                ruleServiceRequest.getPackageName(),
                ruleServiceRequest.getRuleName(),
                ruleServiceRequest.getServiceName(),
                ruleServiceRequest.getEnvironment());
    }



    private void createOrUpdateRule(RuleServiceRequest ruleServiceRequest) {
        createOrUpdateRule(ruleServiceRequest, rulesConfiguration.getKbuilder(), rulesConfiguration.getKbase());
    }

    private void createOrUpdateRule(RuleServiceRequest ruleServiceRequest, KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        LOGGER.info("Adding rule into knowledge base " + ruleServiceRequest);
        createOrUpdateRule(getResource(ruleServiceRequest), kbuilder, kbase);
    }


    private void createOrUpdateRule(Resource resource, KnowledgeBuilder kbuilder, InternalKnowledgeBase kbase) {
        kbuilder.add(resource, ResourceType.DRL);
        kbase.addPackages(kbuilder.getKnowledgePackages());
    }


//    public RuleServiceResponse getResponse() {
//        String q = "package droolsexample\n" +
//                "\n" +
//                "import com.services.micro.rules.search.bl.impl.RulesServiceImpl.Message;\n" +
//                "\n" +
//                "global java.util.List list\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "rule \"GG World\"\n" +
//                "    dialect \"mvel\"\n" +
//                "    when\n" +
//                "        m : Message( status == Message.GOODBYE, message : message )\n" +
//                "    then\n" +
//                "        System.out.println( message );\n" +
//                "    modify ( m ) { message = \"Goodbye cruel world CHANGEDD........\",\n" +
//                "                   status = Message.HELLO };\n" +
//                "end\n";
////        ResourceFactory.newByteArrayResource(q.getBytes());
////        kbuilder.add( ResourceFactory.newClassPathResource( "b.drl") ,  ResourceType.DRL);
//        kbuilder.add( ResourceFactory.newByteArrayResource(q.getBytes()) ,  ResourceType.DRL);
//        kbase.addPackages( kbuilder.getKnowledgePackages() );
//        Collection<KiePackage> pkgs  = kbuilder.getKnowledgePackages();
//        for(KiePackage kiePackage: pkgs) {
//            System.out.println(pkgs);
//            System.out.println("size is " +pkgs.size());
//            System.out.println("name " + kiePackage.getName());
//            Collection<Rule> rules = kiePackage.getRules();
//            for (Rule rule: rules) {
//                System.out.println("rule is " + rule);
//                System.out.println("rule name is " + rule.getName());
//            }
//        }
//            return new RuleServiceResponse();
//    }


    private void validateServiceRequest(RuleServiceRequest ruleServiceRequest) throws Exception {
        if(ruleServiceRequest.getServiceName() == null
                || ruleServiceRequest.getEnvironment() == null
                || ruleServiceRequest.getRuleName() == null
                || ruleServiceRequest.getPackageName() == null) {
            throw new Exception("Missing mandatory fields. All 4 fields serviceName, environment, ruleName and packageName are required");
        }

    }

//    @Autowired
//    private RulesConfigurationProperties rulesConfigurationProperties;
//
//    @Value("${service.myKey1}")
//    private String myKey1;
//
//    @Override
//    @Timed
//    @ExceptionMetered
//    @HystrixCommand(groupKey = "hystrixGroup",
//            commandKey = "helloCommandKey",
//            threadPoolKey = "helloThreadPoolKey",
//            fallbackMethod = "fallbackHello")
//    @Cacheable(cacheNames = "default")
//    @LogExecutionTime
//    public RuleServiceResponse getResponse(String key) {
//        LOGGER.info("getResponse called ");
//        RuleServiceResponse ruleServiceResponse = new RuleServiceResponse();
//
////        ruleServiceResponse.setMessage("Hello " + key + rulesConfigurationProperties.getMyKey1() + "  key1 is " + myKey1);
////        ruleServiceResponse.setPackageName("valid");
////        StatefulKnowledgeSession statelessKnowledgeSession = kbase.newStatefulKnowledgeSession();
////        statelessKnowledgeSession.insert("vijay");
////        statelessKnowledgeSession.fireAllRules();
//        exe(key);
//        return ruleServiceResponse;
//    }
//
//    public RuleServiceResponse fallbackHello(String name) {
//        RuleServiceResponse ruleServiceResponse = new RuleServiceResponse();
////        ruleServiceResponse.setMessage("This is Hello fromm fallback " + name);
//        return ruleServiceResponse;
//    }

    public void exe(String key) {
        KieSession knowledgeSession = null;
        try {

            //knowledgeSession = kContainer.newKieSession("ksession-rules");
            knowledgeSession = rulesConfiguration.getKbase().newKieSession();

            // 4 - create and assert some facts
//            Person rocky = new Person("Rocky Balboa", "Philadelphia", 35);

//            knowledgeSession.insert(rocky);
            final Message message = new Message();
            message.setMessage( "Hello World" );
            if (key.equals("test1")) {
                message.setStatus(Message.HELLO);
            }else {
                message.setStatus(Message.GOODBYE);

            }

            knowledgeSession.insert( message );
//            knowledgeSession.insert("vijay");

            // 5 - fire the rules
            knowledgeSession.fireAllRules();

            System.out.println(message);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            knowledgeSession.dispose();
        }
    }



    public static class Message {
        public static final int HELLO   = 0;
        public static final int GOODBYE = 1;

        private String          message;

        private int             status;

        public Message() {

        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(final int status) {
            this.status = status;
        }

        public static Message doSomething(Message message) {
            return message;
        }

        public boolean isSomething(String msg,
                                   List<Object> list) {
            list.add( this );
            return this.message.equals( msg );
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

}



