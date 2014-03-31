package main.java.edu.gatech;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHandler {
	
	private DBHelper helper;
	private SQLiteDatabase database;
	protected static double curBalance;
	protected static String selectedAccount ="";
	private static final String TABLE_USERS = "Users";
	private static final String TABLE_ACCOUNTS = "Accounts";
	private static final String TABLE_TRANSACTIONS = "Transactions";
	private static String loggedInEmail = User.getLoggedInEmail();
	
	public DBHandler(Context context){
		helper = new DBHelper(context);
	}
	
	public long createUser(String email, String password){
		database = helper.getWritableDatabase();
		Log.d("createUser", "Entering createUser()");
		ContentValues values = new ContentValues();
		values.put("Email", email);
		values.put("Password", password);
		Log.d("createUser", "Creating: " + email + " and " + password);
		long temp = database.insert(TABLE_USERS, null, values);
		return temp;
	}
	
	public Cursor selectUser(String email, String password){
		database = helper.getWritableDatabase();
		Log.d("selectUser", "Entering selectUser()");
		Cursor myCursor = database.rawQuery("select * from Users where Email=? and Password=?" ,new String [] {email, password});
		if (myCursor != null) {
			Log.d("selectUser", "myCursor != null");
		}
		return myCursor;
	}
	
	public long createAccount(String email, String accountName, String balance) {
		database = helper.getWritableDatabase();
		Log.d("createAccount", "Entering createAccount()");
		ContentValues values = new ContentValues();
		values.put("Email", email);
		values.put("AccountName", accountName);
		values.put("Balance", balance);
		Log.d("createAccount", "Creating account: " + email + " and " + accountName + " and $" + balance);
		long temp = database.insert(TABLE_ACCOUNTS, null, values);
		return temp;
	}
	
	public Cursor selectAccount(String email, String accountName) {
		database = helper.getWritableDatabase();
		Log.d("selectAccount", "Entering selectAccount");
		Cursor myCursor = database.rawQuery("select * from " +  TABLE_ACCOUNTS + " where Email=? and AccountName=?", new String[]{email, accountName});
		if (myCursor != null) {
			Log.d("selectAccount", "myCursor != null");
		}
		return myCursor;
	}
	
	public String getAllAccounts() {
		database = helper.getWritableDatabase();
		Log.d("getAllAccounts", "Entering getAllAccounts()");
		Cursor myCursor = database.rawQuery("select * from Accounts where Email = '" + User.getLoggedInEmail() + "'", null);
		String end = "";
		if (myCursor.moveToFirst()) {
			while (!myCursor.isAfterLast()) {
				String name = myCursor.getString(myCursor.getColumnIndex("AccountName"));
				end += name + "\n";
				myCursor.moveToNext();
			}
		}
		Log.d("Accounts: ", end);
		return end;
	}
	
	public List<String> getAccountNames() {
		database = helper.getWritableDatabase();
		List<String> accountNames = new ArrayList<String>();
		Cursor myCursor = database.rawQuery("select * from Accounts", null);
		if(myCursor.moveToFirst()) {
			do {
				accountNames.add(myCursor.getString(myCursor.getColumnIndex("AccountName")));
				Log.d("Account added:", myCursor.getString(myCursor.getColumnIndex("AccountName")));
			} while(myCursor.moveToNext());
		}
		myCursor.close();
		return accountNames;
	}
	
	public double getBalance(String accountName) {
		database = helper.getWritableDatabase();
		Cursor myCursor = database.rawQuery("select balance from Accounts where Email = '" + User.getLoggedInEmail() + "' and AccountName = '" + accountName + "'", null);
		double balance = 0;
		if(myCursor.moveToFirst()) {
			Log.d("Getting Balance: ", myCursor.getString(myCursor.getColumnIndex("Balance")));
		}
		myCursor.close();
		return balance;
	}
	public void withdrawBalance(double amount, String accountName, String destination, String date) {
		database = helper.getWritableDatabase();
		curBalance = getBalance(accountName);
		curBalance -= amount;
		database.execSQL("UPDATE Accounts SET balance = "+ curBalance + " WHERE AccountName = '" + accountName + "'");
		Log.d("New balance", Double.toString(curBalance));
		ContentValues values = new ContentValues();
		values.put("Email", User.getLoggedInEmail());
		values.put("AccountName", accountName);
		values.put("Date", date);
		values.put("Amount", amount);
		values.put("TransactionType",  "W");
		values.put("SourceDestination", destination);
		Log.d("DepositBalance", "Inserting: " + values);
		database.insert(TABLE_TRANSACTIONS, null, values);
	}
	public void depositBalance(double amount, String accountName, String source, String date) {
		database = helper.getWritableDatabase();
		curBalance = getBalance(accountName);
		curBalance += amount;
		database.execSQL("UPDATE Accounts SET balance = " + curBalance + " WHERE AccountName = '" + accountName + "'");
		Log.d("New balance", Double.toString(curBalance));
		ContentValues values = new ContentValues();
		values.put("Email", User.getLoggedInEmail());
		values.put("AccountName", accountName);
		values.put("Date", date);
		values.put("Amount", amount);
		values.put("TransactionType",  "D");
		values.put("SourceDestination", source);
		Log.d("DepositBalance", "Inserting: " + values);
		database.insert(TABLE_TRANSACTIONS, null, values);
	}
	public Cursor getTransactions(String accountName){
		database = helper.getWritableDatabase();
		String email = User.getLoggedInEmail();
		Cursor myCursor = database.rawQuery("select * from " +  TABLE_TRANSACTIONS + " where Email=? and AccountName=?", new String[]{email, accountName});
		return myCursor;
	}
	public String generateSpendingCategoriesReport(String startDate, String endDate){
		database = helper.getWritableDatabase();
		String email = User.getLoggedInEmail();
		String report = "Spending Categories Report for " + email + " from " + startDate + " to " + endDate + "\n-------------------------------------------------\n";
		String sql = "Select SourceDestination, AccountName, sum(amount) from Transactions where email = ? and TransactionType = ? and date between '" + startDate + "' and '" + endDate 
				+ "' group by SourceDestination order by AccountName";
		Cursor myCursor = database.rawQuery(sql, new String[]{email, "W"});
		if (myCursor.moveToFirst()){
			Log.d("ReportPage", "Report query not null!");
			while(!myCursor.isAfterLast()){
				String accountName = myCursor.getString(myCursor.getColumnIndex("AccountName"));
				report +=accountName+":\t";
				String category = myCursor.getString(myCursor.getColumnIndex("SourceDestination"));
				report +=category+" - $";
				int amount = myCursor.getInt(myCursor.getColumnIndex("sum(amount)"));
				report +=amount+"\n";
				Log.d("ReportPage", "Movin to next row");
				myCursor.moveToNext();
			}
			myCursor.close();
		}
		return report;

	}
	
	
	
	
	
	
	
	
	
	
	
}

