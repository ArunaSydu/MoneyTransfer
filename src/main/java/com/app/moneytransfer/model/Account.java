package com.app.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Account Entity
 * @author  Aruna
 */
@Entity
@NoArgsConstructor
@Data
public class Account implements Serializable{

    public Account (String userName, BigDecimal balance, String currencyCode){
        this.userName=userName;
        this.balance=balance;
        this.currencyCode=currencyCode;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long accountId;

    @JsonProperty(required = true)
    private String userName;

    @JsonProperty(required = true)
    private BigDecimal balance;

    @JsonProperty(required = true)
    private String currencyCode;


    public long getAccountId() {
        return accountId;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        if (this.accountId != account.accountId) return false;       
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));        
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userName='" + userName + '\'' +
                ", balance=" + balance +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }

}
