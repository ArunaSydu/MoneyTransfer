package com.app.moneytransfer.dao;

import java.util.List;

import com.app.moneytransfer.model.Account;

/**
 * AccountDao  interface for Accounts
 * @author Aruna
 */
public interface IAccountDao {

    List<Account> getAllAccounts();

}
