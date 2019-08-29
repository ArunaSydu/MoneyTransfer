# MoneyTransfer
Sample application with RestFul Endpoint with below operations supporting concurrent calls

Application starts webserver on http://localhost:8083 by default when LaunchApplication.java is Ran as a Main Application.

Using the Below Tech Stack

Jetty  as a server layer
JAX-RS implementation
JUnit  as a unit test framework
H2  in memory database

How to Run the demo App

Build the project using 

mvn clean test install

Application may be started from standalone jar:

java -jar money-transfer-1.0.0-SNAPSHOT-dependencies.jar

or as a maven goal

mvn exec:java

LaunchApplication creates 2 Accounts , which can be used for Testing

http://localhost:8083/moneyTransfer/account/1

http://localhost:8083/moneyTransfer/account/2

# Available Services
| HTTP METHOD        | PATH           |  USAGE |
| ------------- |:-------------|:-----|
| GET     | /moneyTransfer/account/{accountId} | get account by accountId |
| GET     | /moneyTransfer/account/all  |   get all accounts |
| PUT     | /moneyTransfer/account/create     |  create a new account |
| DELETE  | /moneyTransfer/account/{accountId}     | remove account by accountId |
| POST    | /moneyTransfer/account/{accountId}/withdraw/{amount}    |  withdraw money from account |
| POST    | /moneyTransfer/account/{accountId}/deposit/{amount}     |  deposit money to account |
| POST    | /moneyTransfer/transaction    |  perform transaction between 2 user accounts |


## Sample JSON for Account and Transaction

##### Account:
```json
{  
   "userName":"Test",
   "balance":10.0000,
   "currencyCode":"GBP"
}
```

#####  Transaction:
```json
{  
   "currencyCode":"EUR",
   "amount":100000.0000,
   "fromAccountId":3,
   "toAccountId":4
}
```

Implementation Details

AccountService and FundsTransferService are implementation of the above Rest endpoints also takes concurrency into desing consideration.
TestAccountService and TestFundsTransferService are Testcases which test above Implementation


## Http Status 

```
200 OK: The request has succeeded
```

```
400 Bad Request: The request could not be understood by the server 
```

```
404 Not Found: The requested resource cannot be found
```

```
500 Internal Server Error: The server encountered an unexpected condition





