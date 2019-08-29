package com.app.moneytransfer.service;

import com.app.moneytransfer.dto.UserTransactionDTORequest;
import com.app.moneytransfer.model.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Test class for FundsTransfer
 */
public class TestFundsTransferService extends TestService {
    
    /**
     * Test deposit money to given account number
     *              return 200 OK
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testDeposit() throws IOException, URISyntaxException {       
        URI uri = builder.setPath("/account/"+fromAcctId+"/deposit/100").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterDeposit = mapper.readValue(jsonString, Account.class);        
        assertThat(afterDeposit.getBalance(), is(equalTo(new BigDecimal(105).setScale(2))));
    }
   
    /**
     * Test withdraw money from account given account number, account has sufficient fund
     *                 return 200 OK
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testWithDrawSufficientFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/"+fromAcctId+"/withdraw/1").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterDeposit = mapper.readValue(jsonString, Account.class);
        assertThat(afterDeposit.getBalance(), is(equalTo(new BigDecimal(5).setScale(2,RoundingMode.HALF_EVEN))));
    }

    /**
     * Test withdraw money from account given account number, no sufficient fund in account.
     *                  return 500 INTERNAL SERVER ERROR
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testWithDrawNonSufficientFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/"+fromAcctId+"/withdraw/1000").build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(ERROR_RESPONSE_CODE)));
    }


    /**
     * Test transaction from one account to another with source account has sufficient fund,return 200 
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testTransactionEnoughFund()  {        
        ExecutorService service = Executors.newFixedThreadPool(3);
        FundsTransferTask task = new FundsTransferTask();
        for(int i=0;i<=3;i++) {           
            service.submit(task);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class is used for testing transferFund in multiThreaded Environment.
     */
    class FundsTransferTask implements Runnable {      
        FundsTransferTask() {
        }

        public void run() {
            try {
                URI uri = builder.setPath("/transaction/transferFund").build();
                BigDecimal amount = new BigDecimal(1).setScale(2, RoundingMode.HALF_EVEN);
                UserTransactionDTORequest transaction = new UserTransactionDTORequest("EUR", amount, fromAcctId,toAcctId);
                String jsonInString = mapper.writeValueAsString(transaction);
                StringEntity entity = new StringEntity(jsonInString);
                HttpPost request = new HttpPost(uri);
                request.setHeader("Content-type", "application/json");
                request.setEntity(entity);
                HttpResponse response = client.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
               } catch (IOException | URISyntaxException  e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test transaction from one account to another with source account has no sufficient fund, returns 500
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testTransactionNotEnoughFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction/transferFund").build();
        BigDecimal amount = new BigDecimal(50000000).setScale(2, RoundingMode.HALF_EVEN);
        UserTransactionDTORequest transaction = new UserTransactionDTORequest("EUR", amount, fromAcctId, toAcctId);
        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(ERROR_RESPONSE_CODE)));
    }

}
