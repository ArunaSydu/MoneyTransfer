
import java.util.Random;
public class AccountCustomers {

	public static void main(String[] args) {
		System.out.println("Creating Savings Account");
		SavingsAccount s = new SavingsAccount(0);
		System.out.println("MaxAmount = $2, MaxBalance = $20");
		Customer c[] = new Customer[2];
		for(int i = 0; i<2; i++) {
			System.out.println("Creating Customer [" + (i+1) + "] thread ...");
			c[i] = new Customer(s, i);
			c[i].start();
			
		}

}
}
class Customer extends Thread implements Runnable{
	SavingsAccount s;
    int num;

    public Customer (SavingsAccount s, int n) {
    	this.s= s;
    	this.num= n;
    	
    }

    public void run() {
    	
    	Random rand = new Random();
        while(true){
        	try {
        		int amount = rand.nextInt(2) + 1;
        		s.deposit(amount, num);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
            try {Thread.sleep(500); 
            } catch (Exception e) { return;}
            
            try {
            	int amount1 = rand.nextInt(2) + 1;
            	s.withdraw(amount1, num);
            } catch (InterruptedException e1) {
            	e1.printStackTrace();
            }
            try {Thread.sleep(500); 
            } catch (Exception e) { return;}

        }
    }
	
}
class SavingsAccount{
	protected int balance;
	protected int max = 20;

	public SavingsAccount(int openingBalance){
		balance=openingBalance;
	}
	

	public void deposit(int amount, int n) throws InterruptedException{
		while(amount+balance>max) {
			wait();
			
		}
		balance+=amount;
		System.out.println("Customer [" + (n+1) + "] deposit amount = $" + amount + " [new balance = $" + balance +"]");
		notifyAll();
		
	}
	public void withdraw(int amount, int n) throws InterruptedException{
		while(amount>balance) {
			wait();			
		}
		balance-=amount;
		System.out.println("Customer [" + (n+1) + "] withdraw amount = $" + amount + " [new balance = $" + balance +"]");
		notifyAll();
		
	}
}



