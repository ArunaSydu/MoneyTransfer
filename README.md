# MoneyTransfer
Sample application with RestFul Endpoint with below operations supporting concurrent calls

1.AccountCreation

2.Debit

3.Credit

4.FundsTransfer 

Application starts webserver on http://localhost:8083 by default when LaunchApplication.java is Ran as a Main Application.

Jetty - as a server layer
JAX-RS implementation
JUnit 5 - as a unit test framework
h2database - inmemory databse

Application may be started from standalone jar:

java -jar money-transfer-1.0.0-SNAPSHOT.jar
or as a maven goal

mvn exec:java
Account API - /accounts

POST - persists new account Request Body - Account object

Sample request:

{
	"id":"2",
	"balance":"5.0"
}
Sample response: Status: 200 OK

{
	"id":"2",
	"balance":"5.0"
}
Duplicated account response: Status: 400 Bad Request

Account with ID:2 already exists. 
Duplicates are not allowed.
/{id} - account id GET - retrieves all accounts from database

Response: Status: 200 OK

{
    "id": "97463a3b-8ad9-4ad5-ae7a-37d3807795f7",
    "balance": 55.3
}
Account doesn't exist: Status: 204 No Content

Transaction API - /transactions
POST - submit new transaction

Request Body - MoneyTransfer object

Sample request:

{
	"source":"1",
	"target":"2",
	"balance":"5.0"
}
Sample response: Status: 200 OK

[
    {
        "id": "1",
        "balance": "45"
    },
    {
        "id": "2",
        "balance": "10"
    }
]
Insufficient balance on source account: Status: 409 Conflict

Money Transfer cant be performed due to lack of funds on the account.
One of the party accounts doesn't exist: Status: 400 Bad Request

Account(s) doesnt exist. | Source: null, Target: Account{id=2, balance=10}
