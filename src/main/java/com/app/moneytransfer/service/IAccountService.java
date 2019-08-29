package com.app.moneytransfer.service;

import com.app.moneytransfer.model.Account;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

 interface IAccountService {

        /**
         * Find all accounts
         * @return
         */
         List<Account> getAllAccounts();

        /**
         * Find by account id
         * @param accountId
         * @return
         */
         Account getAccount(@PathParam("accountId") long accountId);

        /**
         * Find balance by account Id
         * @param accountId
         * @return
         */
         BigDecimal getBalance(@PathParam("accountId") long accountId);

        /**
         * Create Account
         * @param account
         * @return
         */
         Account createAccount(Account account);
       
        /**
         * Delete account by account Id
         * @param accountId
         * @return
         */
         Response deleteAccount(@PathParam("accountId") long accountId);

    }


