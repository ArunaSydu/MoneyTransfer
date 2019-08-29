package com.app.moneytransfer.service;

import com.app.moneytransfer.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * UserService Interface
 * @author Aruna
 */
public interface IUserService {

    /**
     * Find by userName
     * @param userName
     * @return
     */
     User getUserByName(@PathParam("userName") String userName) ;

    /**
     * Find by all
     * @return
     */
     List<User> getAllUsers();

    /**
     * Create User
     * @param user
     * @return
     */
    User createUser(User user);

    /**
     * Find by User Id
     * @param userId
     * @param user
     * @return
     */
    User updateUser(@PathParam("userId") long userId, User user);

    /**
     * Delete by User Id
     * @param userId
     * @return
     */
    Response deleteUser(@PathParam("userId") long userId);
}
