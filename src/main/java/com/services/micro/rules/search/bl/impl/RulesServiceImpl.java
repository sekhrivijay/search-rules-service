package com.services.micro.rules.search.bl.impl;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.services.micro.commons.logging.annotation.LogExecutionTime;
import com.services.micro.rules.search.api.response.ServiceResponse;
import com.services.micro.rules.search.bl.RulesService;
import com.services.micro.rules.search.config.RulesConfigurationProperties;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "RulesService")
@EnableConfigurationProperties(RulesConfigurationProperties.class)
public class RulesServiceImpl implements RulesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesServiceImpl.class);

    private  KieContainer kContainer;
    private KieSession ks;
    private  InternalKnowledgeBase kbase;
    private KnowledgeBuilder kbuilder;
//
    public RulesServiceImpl() {
        try {
            // load up the knowledge base
            kbase = KnowledgeBaseFactory.newKnowledgeBase();
            kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kbuilder.add( ResourceFactory.newClassPathResource( "a.drl") ,  ResourceType.DRL);

            kbase.addPackages( kbuilder.getKnowledgePackages() );

//            KieSession ks = kbase.newKieSession();
//            KieServices ks = KieServices.Factory.get();
//            kContainer = ks.getKieClasspathContainer();
//            KieScanner kieScanner = ks.newKieScanner(kContainer);
//            kieScanner.start(2000L);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public ServiceResponse getResponse() {
        String q = "package droolsexample\n" +
                "\n" +
                "import com.services.micro.rules.search.bl.impl.RulesServiceImpl.Message;\n" +
                "\n" +
                "global java.util.List list\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "rule \"GG World\"\n" +
                "    dialect \"mvel\"\n" +
                "    when\n" +
                "        m : Message( status == Message.GOODBYE, message : message )\n" +
                "    then\n" +
                "        System.out.println( message );\n" +
                "    modify ( m ) { message = \"Goodbye cruel world CHANGEDD........\",\n" +
                "                   status = Message.GOODBYE };\n" +
                "end\n";
        ResourceFactory.newByteArrayResource(q.getBytes());
//        kbuilder.add( ResourceFactory.newClassPathResource( "b.drl") ,  ResourceType.DRL);
        kbuilder.add( ResourceFactory.newByteArrayResource(q.getBytes()) ,  ResourceType.DRL);
        kbase.addPackages( kbuilder.getKnowledgePackages() );

            return new ServiceResponse();
    }



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

//        serviceResponse.setMessage("Hello " + key + rulesConfigurationProperties.getMyKey1() + "  key1 is " + myKey1);
//        serviceResponse.setType("valid");
//        StatefulKnowledgeSession statelessKnowledgeSession = kbase.newStatefulKnowledgeSession();
//        statelessKnowledgeSession.insert("vijay");
//        statelessKnowledgeSession.fireAllRules();
        exe(key);
        return serviceResponse;
    }

    public ServiceResponse fallbackHello(String name) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("This is Hello fromm fallback " + name);
        return serviceResponse;
    }

    private void exe(String key) {
        KieSession knowledgeSession = null;
        try {

            //knowledgeSession = kContainer.newKieSession("ksession-rules");
            knowledgeSession = kbase.newKieSession();

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



