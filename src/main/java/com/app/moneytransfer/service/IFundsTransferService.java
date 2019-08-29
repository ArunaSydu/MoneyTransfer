package com.app.moneytransfer.service;

import com.app.moneytransfer.dto.UserTransactionDTORequest;

import javax.ws.rs.core.Response;

/**
 * TransactionService Interface to TransferFunds
 */
public interface IFundsTransferService {

    /**
     * 
     * @param transaction
     * @return
     */
    Response transferFund(UserTransactionDTORequest transaction) throws Exception;

    }
