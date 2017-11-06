package com.services.micro.rules.search.test;

import com.micro.services.search.api.SearchModelWrapper;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.services.micro.rules.search.bl.impl.RulesServiceImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);


    public void getResponse() {
        KieSession knowledgeSession = null;
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        String q = "package DE\n" +
                "\n" +
                "import com.services.micro.rules.search.bl.impl.RulesServiceImpl.Message;\n" +
                "\n" +
                "global java.util.List list\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "rule \"GGGG World\"\n" +
                "    dialect \"mvel\"\n" +
                "    when\n" +
                "        m : Message( status == Message.GOODBYE, message : message )\n" +
                "    then\n" +
                "        System.out.println( message );\n" +
                "    modify ( m ) { message = \"Goodbye cruel world CHANGEDD........\",\n" +
                "                   status = Message.HELLO };\n" +
                "end\n";

        String w = "package com.services.micro.rules.search.test\n" +
                " \n" +
                "import com.services.micro.rules.search.bl.impl.RulesServiceImpl.Message;\n" +
                " \n" +
                "rule \"Hello World\"\n" +
                "    when\n" +
                "        m : Message( status == Message.HELLO, message : message)\n" +
                "    then\n" +
                "        System.out.println( message );\n" +
                "end";


        String r ="package droolsexample\n" +
                "\n" +
                "import com.micro.services.search.api.request.SearchServiceRequest;\n" +
                "import com.micro.services.search.api.response.SearchServiceResponse;\n" +
                "import com.micro.services.search.api.response.Redirect;\n" +
                "\n" +
                "rule \"RedirectFlower\"\n" +
                "    dialect \"java\"\n" +
                "    when\n" +
                "        req : SearchServiceRequest(q == 'roses')\n" +
                "        res : SearchServiceResponse()\n" +
                "    then\n" +
                "        System.out.println( req );\n" +
                "\t\tRedirect redirect = new Redirect();\n" +
                "\t\tredirect.setRedirectUrl(\"http://abc.com/flowers-cat/roses\");\n" +
                "\t\tres.setRedirect(redirect);\n" +
                "end";
//        ResourceFactory.newByteArrayResource(q.getBytes());
//        kbuilder.add( ResourceFactory.newClassPathResource( "b.drl") ,  ResourceType.DRL);
        kbuilder.add(ResourceFactory.newByteArrayResource(q.getBytes()), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newByteArrayResource(w.getBytes()), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newByteArrayResource(r.getBytes()), ResourceType.DRL);
        kbase.addPackages(kbuilder.getKnowledgePackages());
        Collection<KiePackage> pkgs = kbuilder.getKnowledgePackages();
        for (KiePackage kiePackage : pkgs) {
            System.out.println(pkgs);
            System.out.println("size is " + pkgs.size());
            System.out.println("name " + kiePackage.getName());
            Collection<Rule> rules = kiePackage.getRules();
            for (Rule rule : rules) {
                System.out.println("rule is " + rule);
                System.out.println("rule name is " + rule.getName());
            }
        }


        knowledgeSession = kbase.newKieSession();
        final RulesServiceImpl.Message message = new RulesServiceImpl.Message();
        message.setMessage("Hello aaaaaWorld");
        message.setStatus(RulesServiceImpl.Message.HELLO);

        SearchModelWrapper searchModelWrapper = new SearchModelWrapper();
        SearchServiceRequest searchServiceRequest = new SearchServiceRequest();
        searchServiceRequest.setQ("roses");
        SearchServiceResponse searchServiceResponse = new SearchServiceResponse();
        searchModelWrapper.setSearchServiceResponse(searchServiceResponse);
        searchModelWrapper.setSearchServiceRequest(searchServiceRequest);


//        knowledgeSession.insert( message );
        knowledgeSession.insert( searchServiceRequest );
        knowledgeSession.insert( searchServiceResponse );
        knowledgeSession.fireAllRules();
//        System.out.println("OUTSIDE.................." + message);
        System.out.println("OUTSIDE.................." + searchServiceRequest);
        System.out.println("OUTSIDE.................." + searchServiceResponse);




    }


    public SearchModelWrapper fireRules(SearchModelWrapper searchModelWrapper) {
        KieSession knowledgeSession = null;
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        String rule = "package droolsexample\n" +
                "\n" +
                "import com.services.micro.rules.search.bl.impl.Message;\n" +
                "\n" +
                "rule \"Good Bye\"\n" +
                "      dialect \"java\"\n" +
                "  when\n" +
                "      Message( status == Message.HELLO, message : message )\n" +
                "  then\n" +
                "      System.out.println( message ); \n" +
                "end";
        kbuilder.add(ResourceFactory.newByteArrayResource(rule.getBytes()), ResourceType.DRL);
        kbase.addPackages(kbuilder.getKnowledgePackages());
        try {
            knowledgeSession = kbase.newKieSession();
//            knowledgeSession.insert(searchModelWrapper.getSearchServiceRequest());
//            knowledgeSession.insert(searchModelWrapper.getSearchServiceResponse());

            final RulesServiceImpl.Message message = new RulesServiceImpl.Message();
            message.setMessage("Hello World");
            message.setStatus(RulesServiceImpl.Message.HELLO);
            knowledgeSession.insert(message);

            knowledgeSession.fireAllRules();

            LOGGER.info(searchModelWrapper.toString());
        } catch (Throwable t) {
            LOGGER.error("Could not execute rule ", t);
        } finally {
            if (knowledgeSession != null) {
                knowledgeSession.dispose();
            }
        }
        return searchModelWrapper;
    }

    public static void main(String[] args) {
        SearchModelWrapper searchModelWrapper = new SearchModelWrapper();
        SearchServiceRequest searchServiceRequest = new SearchServiceRequest();
        searchServiceRequest.setQ("roses");
        SearchServiceResponse searchServiceResponse = new SearchServiceResponse();
        searchModelWrapper.setSearchServiceResponse(searchServiceResponse);
        searchModelWrapper.setSearchServiceRequest(searchServiceRequest);
        Test test = new Test();
//        test.fireRules(searchModelWrapper);

        test.getResponse();
    }
}
