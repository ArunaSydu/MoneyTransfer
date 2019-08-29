package com.app.moneytransfer.exception;

/**
 * Custom UserValidationException
 * @author Aruna.
 */
public class UserValidationException extends Exception {

    private String errorMessage;
    private int errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorNo() {
        return errorCode;
    }
    /**
     * 
     * @param message
     */
    public UserValidationException(String message){
    this.errorMessage=message;        
    }

    /**
     * 
     * @param errorCode
     * @param errorMessage
     */
    public UserValidationException(int errorCode,String errorMessage){
        this.errorMessage=errorMessage;
        this.errorCode=errorCode;
    }

}
