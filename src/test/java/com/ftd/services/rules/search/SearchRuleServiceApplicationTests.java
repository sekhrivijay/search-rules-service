package com.ftd.services.rules.search;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.net.URL;
import java.util.Arrays;
import java.util.Base64;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftd.services.rules.search.api.RuleEntity;
import com.ftd.services.rules.search.api.Status;
import com.ftd.services.rules.search.exception.SearchExceptionHandler.TranslatedExceptionMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SearchRuleServiceApplication.class)
public class SearchRuleServiceApplicationTests {

    @LocalServerPort
    private int              port;
    private URL              base;

    @Inject
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        /*
         * This happens before each test
         */
        this.base = new URL("http://localhost:" + port + "/api/rules");
        deleteAllJunitDatabaseEntries();
    }

    private void deleteAllJunitDatabaseEntries() {
        ResponseEntity<RuleEntity[]> response = template.getForEntity(
                base.toString() + "/",
                RuleEntity[].class);
        Arrays.stream(response.getBody())
                .filter(each -> each.getPackageName().startsWith("junit"))
                .forEach(rule -> {
                    template.delete(base.toString() + "/{1}", rule.getId());
                });
    }

    @Test
    public void createRule() throws Exception {

        RuleEntity rule = new RuleEntity();
        rule.setPackageName("junitPackage");
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName("junitRuleName");
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));

        ResponseEntity<RuleEntity> createResponse = template.postForEntity(
                base.toString() + "/",
                rule,
                RuleEntity.class);
        Assert.assertEquals("checking for 200 OK", OK, createResponse.getStatusCode());
        /*
         * get all
         */
        ResponseEntity<RuleEntity[]> findAllResponse = template.getForEntity(
                base.toString() + "/",
                RuleEntity[].class);
        Assert.assertEquals("checking for 200 OK", OK, findAllResponse.getStatusCode());

        long newItemCount = Arrays.stream(findAllResponse.getBody())
                .filter(each -> each.getPackageName().startsWith("junit")).count();
        Arrays.stream(findAllResponse.getBody())
                .filter(each -> each.getPackageName().startsWith("junit"))
                .forEach(dbrule -> Assert.assertEquals("base64Check", rule.getRule(), dbrule.getRule()));

        Assert.assertEquals("new created count", 1, newItemCount);
    }

    @Test
    public void createRuleWithNoPackage() throws Exception {

        RuleEntity rule = new RuleEntity();
        rule.setPackageName(null);
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName("junitRuleName");
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));

        ResponseEntity<TranslatedExceptionMessage> badDataResponse = template.postForEntity(
                base.toString() + "/",
                rule,
                TranslatedExceptionMessage.class);
        Assert.assertEquals("checking for bad data", BAD_REQUEST, badDataResponse.getStatusCode());
        Assert.assertEquals("checking for bad data message",
                "Missing mandatory fields. Both fields ruleName and packageName are required",
                badDataResponse.getBody().getMessage());
    }

    @Test
    public void createRuleWithNoRuleName() throws Exception {

        RuleEntity rule = new RuleEntity();
        rule.setPackageName("junitPackage");
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName(null);
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));

        ResponseEntity<TranslatedExceptionMessage> badDataResponse = template.postForEntity(
                base.toString() + "/",
                rule,
                TranslatedExceptionMessage.class);
        Assert.assertEquals("checking for bad data", BAD_REQUEST, badDataResponse.getStatusCode());
        Assert.assertEquals("checking for bad data message",
                "Missing mandatory fields. Both fields ruleName and packageName are required",
                badDataResponse.getBody().getMessage());
    }

    @Test
    public void createDuplicateRule() throws Exception {

        RuleEntity rule = new RuleEntity();
        rule.setPackageName("junitPackage");
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName("junitRuleName");
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));

        template.postForEntity(
                base.toString() + "/",
                rule,
                RuleEntity.class);
        ResponseEntity<TranslatedExceptionMessage> duplicateResponse = template.postForEntity(
                base.toString() + "/",
                rule,
                TranslatedExceptionMessage.class);
        Assert.assertEquals("checking for duplicate", BAD_REQUEST, duplicateResponse.getStatusCode());
        Assert.assertEquals("checking for duplicate message",
                "A rule with same ruleName, packageName, serviceName, and environment already exist",
                duplicateResponse.getBody().getMessage());
    }

    @Test
    public void deleteRule() throws Exception {
        /*
         * create it first
         */
        RuleEntity rule = new RuleEntity();
        rule.setPackageName("junitPackage");
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName("junitRuleName");
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));

        ResponseEntity<RuleEntity> createResponse = template.postForEntity(
                base.toString() + "/",
                rule,
                RuleEntity.class);
        RuleEntity createdRule = createResponse.getBody();
        /*
         * delete it
         */
        template.delete(base.toString() + "/{1}", createdRule.getId());
        /*
         * find it
         */
        ResponseEntity<RuleEntity> queryresponse = template.getForEntity(
                base.toString() + "/{1}",
                RuleEntity.class,
                createdRule.getId());

        Assert.assertEquals("checking for 404 notfound", NOT_FOUND, queryresponse.getStatusCode());
    }

    @Test
    public void deleteRuleThatDoesntExist() throws Exception {
        RuleEntity rule = new RuleEntity();
        /*
         * delete it
         */
        HttpEntity<RuleEntity> entity = new HttpEntity<RuleEntity>(rule);
        ResponseEntity<TranslatedExceptionMessage> updateResponse = template.exchange(
                base.toString() + "/1234",
                HttpMethod.DELETE,
                entity,
                TranslatedExceptionMessage.class, rule);

        Assert.assertEquals("checking for 404", NOT_FOUND, updateResponse.getStatusCode());
        Assert.assertEquals("checking for not found message",
                "rule 1234 not found in database on delete",
                updateResponse.getBody().getMessage());
    }

    @Test
    public void forceReload() throws Exception {
        template.delete(base.toString() + "/reloadKb");
    }

    @Test
    public void updateUnknownRule() throws Exception {
        /*
         * first create it but don't store it
         */
        RuleEntity rule = new RuleEntity();
        rule.setPackageName("junitPackage");
        rule.setStatus(Status.INACTIVE);
        rule.setRuleName("junitRuleName");
        rule.setRule(Base64.getEncoder().encodeToString("jUnit Rule Contents".getBytes()));
        /*
         * update it
         */
        rule.setRule(Base64.getEncoder().encodeToString("jUnit UPDATED Rule Contents".getBytes()));
        HttpEntity<RuleEntity> entity = new HttpEntity<RuleEntity>(rule);
        ResponseEntity<TranslatedExceptionMessage> updateResponse = template.exchange(
                base.toString() + "/1234",
                HttpMethod.PUT,
                entity,
                TranslatedExceptionMessage.class, rule);

        Assert.assertEquals("checking for 404", NOT_FOUND, updateResponse.getStatusCode());
        Assert.assertEquals("checking for not found message",
                "rule 1234 not found in database on update",
                updateResponse.getBody().getMessage());
    }
}
