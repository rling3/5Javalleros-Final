package main.java.edu.gatech;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Transaction {

	private Date timeStamp;
	private double amount;
	private Account account;
	public static enum TRANSACTION_TYPE {WITHDRAW, DEPOSIT};
	
	public Transaction(Account account, double amount, TRANSACTION_TYPE type) {
		if (account != null) {
			if (!(type == TRANSACTION_TYPE.WITHDRAW && amount > account.getBalance())) {
				this.account = account;
				this.amount = amount;
				timeStamp = new Date();
			} else {
				
			}
		} else {
			
		}
	}
	
	public double getAmount() {
		return amount;
	}
	
	public Date getTimestamp() {
		return new Date(timeStamp.getTime());
	}
	
	public Account getAssociatedAccount() {
		return account;
	}
	
}
