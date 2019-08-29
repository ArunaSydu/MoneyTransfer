package com.app.moneytransfer.service;

import com.app.moneytransfer.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *  Class to unit test  UserService methods
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserService extends TestService {
    
     /**
     * This method is used to create a User , checks if the Response is 200 and Asserts the returned User 
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testACreateUser() throws Exception {   
        final String path = "/user/create";
        User user = new User("Aruna","email-test@gmail.com");
        final HttpPost request = createRequest(path,user);       
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        User userAfterCreation = mapper.readValue(jsonString, User.class);        
        assertThat(userAfterCreation.getUserName(), is(equalTo("Aruna")));
        assertThat(userAfterCreation.getEmailAddress(), is(equalTo("email-test@gmail.com")));
    }

    /**
     * This method is used to get the created User.checks if the Response is 400.
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testCreateExistingUser() throws IOException, URISyntaxException {
        final String path = "/user/create";
        User user = new User("Aruna","email-test@gmail.com");
        final HttpPost request = createRequest(path,user);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(BAD_REQUEST_RESPONSE_CODE)));
    }

    /**
     * This method is used to get the created User.checks if the Response is 200 and Asserts the returned User
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testAGetUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/Aruna").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        User user = mapper.readValue(jsonString, User.class);
        assertThat(user.getUserName(), is(equalTo("Aruna")));
        assertThat(user.getEmailAddress(), is(equalTo("email-test@gmail.com")));

    }

    /**
     * 
     * @param path
     * @param user
     * @return
     * @throws Exception
     */
    private HttpPost createRequest(String path,User user)  throws IOException, URISyntaxException {
        URI uri = builder.setPath(path).build();        
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        return request;
    }


    /**
     * Test to get All Users.
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testZGetAllUsers() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
        String jsonString = EntityUtils.toString(response.getEntity());
        User[] users = mapper.readValue(jsonString, User[].class);
        assertThat(users, not(emptyArray()));
    }
   
    /**
     * Update Existing User using JSON provided from client return 200 OK
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testUpdateUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/1").build();
        User user = new User(1L, "updatedUser", "updatedEMail@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
    }

    /**
     * Test to update Non existant User , returns 404.
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testUpdateNonExistingUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/1000").build();
        User user = new User(1000L, "updatedUser", "updatedEMail@gmail.com");
        String jsonInString = mapper.writeValueAsString(user);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        String jsonString = EntityUtils.toString(response.getEntity());
        User userAfterUpdate = mapper.readValue(jsonString, User.class);
        assertThat(userAfterUpdate.getUserId(), is(equalTo(0L)));
    }

    /**
     * Test Delete User
     * @throws IOException
     * @throws URISyntaxException
     */
    //@Test
    public void testDeleteUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/1").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(SUCCESSFUL_RESPONSE_CODE)));
    }

    /**
     * Test delete Non existant User, Return 404 status code
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testDeleteNonExistingUser() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/user/300").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertThat(statusCode, is(equalTo(NOT_FOUND_RESPONSE_CODE)));
    }


}
