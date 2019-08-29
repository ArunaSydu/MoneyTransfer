package com.app.moneytransfer.service;

import com.app.moneytransfer.dao.AccountDAOImpl;
import com.app.moneytransfer.dto.UserTransactionDTORequest;
import com.app.moneytransfer.exception.UserValidationException;
import com.app.moneytransfer.model.Account;
import com.app.moneytransfer.util.HibernateUtil;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Aruna
 * TransactionService 
 */
@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class FundsTransferService implements IFundsTransferService {

    private final AccountService accountService = new AccountService();
    private static ReentrantLock transferLock = new ReentrantLock();
    
    /**
	 * Transfer fund between two accounts.
	 * @param transaction
	 * @return
	 */   
	@POST
    @Path("/transferFund")
	public  Response transferFund(UserTransactionDTORequest transaction) throws Exception { 
        return transferFundFromTo(transaction.getFromAccountId(),transaction.getToAccountId(),transaction.getAmount());        
      }


    
    /**
     * This method checks if the FromAcc and ToAcc are valid,Synchronises critical block as this is accessed by Multiple Clients
     * and as in real time scenario database contains the latest updated From and To Account balance 
     * @param fromAccountNo
     * @param toAccountNo
     * @param amount
     * @throws Exception
     */
    private Response transferFundFromTo(Long fromAccountNo,Long toAccountNo,BigDecimal amount ) throws Exception {        
        Account creditAccount = null;
        Account debitAccount = null;
        final int validationFailedErrorCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        Account toAccount = accountService.getAccount(toAccountNo);
            if(toAccount == null) {
                throw new UserValidationException(validationFailedErrorCode,"ToAccount is invalid");
            }                 
            Account fromAccount = accountService.getAccount(fromAccountNo);
            if (fromAccount == null) {
                throw new UserValidationException(validationFailedErrorCode, "FromAccount is Invalid");
            }
            if (fromAccount.getBalance().compareTo(BigDecimal.ZERO.setScale(2,RoundingMode.HALF_EVEN))<=0) {
                throw new UserValidationException(validationFailedErrorCode, "Insufficient Balance to Transfer ");
            }
            System.out.println("original Balance  "
                    + Thread.currentThread().getName() + " " +fromAccount.getBalance());
            if (fromAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN)) < 0) {
                throw new UserValidationException(validationFailedErrorCode, "Insufficient Balance to Transfer ");
            }
            //This is critical section. Need to executed in isolation.
        try {
            transferLock.lock();
            debitAccount = accountService.updateAccountBalance(fromAccountNo, amount.negate());
            if (debitAccount != null) {
            creditAccount = accountService.updateAccountBalance(toAccountNo, amount);
            }
            System.out.println("deducted Balance  "
                    + Thread.currentThread().getName() + " " +debitAccount.getBalance());
            }finally {
                transferLock.unlock();
            }
       if(debitAccount!=null && creditAccount!=null)           
            return Response.status(Response.Status.OK).build();        
        else return Response.status(Response.Status.PRECONDITION_FAILED).build();     
      }
        
    }
