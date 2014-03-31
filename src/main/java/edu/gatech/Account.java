package main.java.edu.gatech;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Account implements Serializable {
	private String name;
	private double balance;
	private Collection<Transaction> history;
	
	public Account() {
		history = new ArrayList<Transaction>();
	}
	
	public void applyTransaction(Transaction transaction) {
		history.add(transaction);
		balance += transaction.getAmount();
	}
	
	public double getBalance() {
		return balance;
	}
	
}
