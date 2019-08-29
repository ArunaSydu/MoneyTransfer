package com.app.moneytransfer.service;

import com.app.moneytransfer.dao.AccountDAOImpl;
import com.app.moneytransfer.model.Account;
import com.app.moneytransfer.util.HibernateUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

;

/**
 * Account Service 
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService implements IAccountService {
	
    private final AccountDAOImpl accountDAO = new AccountDAOImpl(HibernateUtil.getSessionFactory());
    private static ReentrantLock reentrantLock = new ReentrantLock();
    
     
    /**
     * Find all accounts
     * @return
     */
    @GET
    @Path("/all")
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Find by account id
     * @param accountId
     * @return
     */
    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") long accountId){
        Account myAccount = null;       
        reentrantLock.lock();
        myAccount = accountDAO.findById(accountId, Account.class);
        reentrantLock.unlock();            
        return myAccount;
    }
    
    /**
     * Find balance by account Id
     * @param accountId
     * @return
     */
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") long accountId) {
        Account account = getAccount(accountId);
        if(account == null){
            throw new WebApplicationException("Account not found " + accountId, Response.Status.NOT_FOUND);
        }
        return account.getBalance();
    }
    
    /**
     * Create Account
     * @param account
     * @return
     */
    @PUT
    @Path("/create")
    public Account createAccount(Account account){
        accountDAO.save(account);
        return accountDAO.findById(account.getAccountId(), Account.class);
    }

    /**
     * Deposit amount by account Id
     * @param accountId
     * @param amount
     * @return
     */
    @POST
    @Path("/{accountId}/deposit/{amount}")
    public Account deposit(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        return updateAccountBalance(accountId, amount);
    }

    /**
     * Withdraw amount by account Id
     * @param accountId
     * @param amount
     * @return
     */
    @POST
    @Path("/{accountId}/withdraw/{amount}")
    public Account withdraw(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <=0){
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        Account account = accountDAO.findById(accountId, Account.class);
        if (account != null) {
            BigDecimal balance = account.getBalance();
            if (balance.compareTo(amount) < 0) {
                throw new WebApplicationException("Not sufficient Fund for account: " + accountId);
            }
            return updateAccountBalance(accountId, amount.negate());
        }
        else
        {
            throw new WebApplicationException("Account not found: " + accountId);
        }
    }

    /**
     * 
     * @param accountId
     * @param amount
     * @return
     */
    public Account updateAccountBalance(long accountId, BigDecimal amount){ 
        Account updatedAcc = null;
        Account targetAccount = getAccount(accountId);
        try {
            reentrantLock.lock();
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
            updatedAcc = accountDAO.update(targetAccount);
        }finally {
            reentrantLock.unlock();
        }       
        return updatedAcc;
    }

    /**
     * Delete account by account Id
     * @param accountId
     * @return
     */
    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId){
        try {
            accountDAO.delete(getAccount(accountId));
            return Response.status(Response.Status.OK).build();
        }
       catch (Exception e) {
           return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
