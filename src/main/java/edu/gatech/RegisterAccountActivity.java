package main.java.edu.gatech;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterAccountActivity extends Activity {
	private String rAccName;
	private String balance;
	protected String loggedInEmail = User.getLoggedInEmail();
	private EditText rAccNameField;
	private EditText rBalNameField;
	private AccountRegisterTask accRegTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);
		
		rAccNameField = (EditText) findViewById(R.id.accname_box);
		rAccNameField.setText(rAccName);
		rBalNameField = (EditText) findViewById(R.id.balanceBox);
		
		Button btn = (Button) findViewById(R.id.accountBut);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptReg();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_account, menu);
		return true;
	}
	
	public void attemptReg() {
		rAccNameField.setError(null);
		rBalNameField.setError(null);
		boolean cancel = false;
		View focusView = null;
		
		rAccName = rAccNameField.getText().toString();
		balance = rBalNameField.getText().toString();
		if (TextUtils.isEmpty(rAccName)) {
			rAccNameField.setError(getString(R.string.error_field_required));
			focusView = rAccNameField;
			cancel = true;
		}
		if (TextUtils.isEmpty(balance)) {
			rBalNameField.setError(getString(R.string.error_field_required));
			focusView = rBalNameField;
			cancel = true;
		}
		for(int i = 0; i < balance.length(); i++) {
			if(balance.substring(i,i+1).equals(".")) {
				if(balance.substring(i+1).length() > 2) {
					rBalNameField.setError("Error: Not a valid Balance");
					focusView = rBalNameField;
					cancel = true;
				}
			}
		}
		if (cancel) {
			focusView.requestFocus();
		}
		else {
			accRegTask = new AccountRegisterTask();
			accRegTask.execute((Void) null);
		}
	}

	/**
	 * An asynchronous task for registering a new account associated with an email address.
	 */
	private class AccountRegisterTask extends AsyncTask<Void, Void, Boolean>{
		private DBHandler database = new DBHandler(RegisterAccountActivity.this);

		@Override
		protected Boolean doInBackground(Void... args) {
			database.createAccount(loggedInEmail, rAccName, balance);
			Log.d("Accounts:", database.getAllAccounts());
			return true;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			accRegTask = null;
			
			if (success) {
				Intent i=new Intent(
						RegisterAccountActivity.this,
						HomePageActivity.class);
				startActivity(i);
			} else {
				rAccNameField.setError("Failed to register new account.");
				rAccNameField.requestFocus();
			}
		}
		
		@Override
		protected void onCancelled() {
			accRegTask = null;
		}
	}
}
