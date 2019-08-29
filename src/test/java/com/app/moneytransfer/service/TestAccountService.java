package com.app.moneytransfer.service;

import com.app.moneytransfer.model.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * This class test the methods of AccountService
 */
public class TestAccountService extends TestService {


    /**
     * test get user account, return 200 OK
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testGetAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/"+fromAcctId).build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        Account account = mapper.readValue(jsonString, Account.class);        
        assertTrue(account.getUserName().equals("TestTrax"));
    }

    /**
     * test get all user account, return 200 OK
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));

        String jsonString = EntityUtils.toString(response.getEntity());
        Account[] accounts = mapper.readValue(jsonString, Account[].class);
        assertThat(accounts, not(emptyArray()));
    }

    /**
     * test get account balance given account ID, return 200.
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testGetAccountBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1/balance").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String balance = EntityUtils.toString(response.getEntity());
        BigDecimal res = new BigDecimal(balance).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal db = new BigDecimal(10).setScale(2, RoundingMode.HALF_EVEN);
        //assertTrue(res.equals(db));
    }

    
    /**
     * Test to create New Account.
     * @throws IOException
     * @throws URISyntaxException
     */
    @Before
    @Test
    public void testCreateAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        BigDecimal balance = new BigDecimal(10).setScale(2, RoundingMode.HALF_EVEN);
        Account acc = new Account("createdAccount", balance, "CNY");
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));

        String jsonString = EntityUtils.toString(response.getEntity());
        Account aAfterCreation = mapper.readValue(jsonString, Account.class);
        assertThat(aAfterCreation.getUserName(), is(equalTo("createdAccount")));
        assertThat(aAfterCreation.getCurrencyCode(), is(equalTo("CNY")));
    }

    /**
     * Delete valid user account , returns 200
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/6").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
    }

    /**
     * Test to delete non-existent account. return 404 NOT FOUND
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/300").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(NOT_FOUND_RESPONSE_CODE)));
    }

}
