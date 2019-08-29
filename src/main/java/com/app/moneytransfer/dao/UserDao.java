package com.app.moneytransfer.dao;

import java.util.List;

import com.app.moneytransfer.model.User;

/**
 * @author Aruna
 * 
 * UserDao interface 
 */
public interface UserDao {

    List<User> getAllUsers();

    User getUserByName(String userName);

}
