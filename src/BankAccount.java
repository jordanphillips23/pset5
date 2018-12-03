/**
 * Just like last time, the BankAccount class is primarily responsible
 * for depositing and withdrawing money. In the enhanced version, there
 * will be the added requirement of transfering funds between accounts.
 * 
 * Most of the functionality for this class should have already been
 * implemented last time. You can always reference my Github repository
 * for inspiration (https://github.com/rwilson-ucvts/java-sample-atm).
 */

public class BankAccount {
	private User user;
	private String accountNumber;
	private double balance;
	private String open;
	
	public BankAccount(User user, String accountNumber, double balance, String open) {
		this.user = user;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.open = open;
	}
	
	public User getUser() {
		return user;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double amount) {
		this.balance = (double) Math.round(amount * 100) / (double) 100;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public String getOpen() {
		return open;
	}
	
	public void setOpen(String open) {
		this.open = open;
	}
	
	public void deposit(double amount) {
		if (balance + amount < 999999999999.99) {
			balance += amount;
		}
		else {
			balance = 999999999999.99;
		}
		
		
	}
	
	public void withdraw(double amount) {
		balance -= amount;
	}
}