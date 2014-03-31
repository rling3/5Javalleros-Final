package main.java.edu.gatech;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddBalance extends Activity {
	
	private EditText rAmnNameField;
	private String amount, source, date;
	protected double transAmnt;
	private BalanceAdd balAdd;
	private static final int TRANSBUT_ID = 1;
	int month, year, day;
	
	public AddBalance() {
		final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_balance2);
		DBHandler database = new DBHandler(getApplicationContext());
		TextView accountView = (TextView) findViewById(R.id.accountView2);
		rAmnNameField = (EditText) findViewById(R.id.balanceBox);
		accountView.setText("Account: " + database.selectedAccount);
		Button cancelBut = (Button) findViewById(R.id.cancelBut2);
		final EditText sourceField = (EditText)findViewById(R.id.EditText01);
		sourceField.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sourceField.setText("");
				
			}
		});
		cancelBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked return button");
				Intent i = new Intent(AddBalance.this, AccountsPageActivity.class);
				startActivity(i);
			}
		});
		Button addBut = (Button) findViewById(R.id.addBut);
		addBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked add button");
				source = ((EditText)findViewById(R.id.EditText01)).getText().toString();
				attemptAdd();
			}
		});
		Button transBut = (Button) findViewById(R.id.transBut);
		transBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked trans button");
				showDialog(TRANSBUT_ID);
			}
		});
	}
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the TimePickerDialog in the dialog
                public void onDateSet(DatePicker view, int yearSelected,
                        int monthOfYear, int dayOfMonth) {
                    year = yearSelected; 
                    month = monthOfYear;
                    day = dayOfMonth;
                    // Set the Selected Date in Select date Button
                    Button transBut = (Button) findViewById(R.id.transBut);
                    transBut.setText("Date selected :"+day+"-"+month+"-"+year);
                    date = Utils.getDate(month, year, day);
                  }
            };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_balance, menu);
		return true;
	}
	protected Dialog onCreateDialog(int id) {
       	 return new DatePickerDialog(this,
                    mDateSetListener,
                    year, month, day);
    }
	public void attemptAdd() {
		DBHandler database = new DBHandler(getApplicationContext());
		double accountAmount;
		rAmnNameField.setError(null);
		amount = rAmnNameField.getText().toString();
		boolean cancel = false;
		View focusView = null;
		accountAmount = database.getBalance(database.selectedAccount);
		Log.d("amount: ", Double.toString(database.getBalance(database.selectedAccount)));
		if(TextUtils.isEmpty(amount)) {
			rAmnNameField.setError(getString(R.string.error_field_required));
			focusView = rAmnNameField;
			cancel = true;
		}
		for(int i = 0; i < amount.length(); i++) {
			if(amount.substring(i,i+1).equals(".")) {
				if(amount.substring(i+1).length() > 2) {
					rAmnNameField.setError("Error: Not a valid Balance");
					focusView = rAmnNameField;
					cancel = true;
				}
			}
		}
		if (cancel) {
			focusView.requestFocus();
		}
		else {
			transAmnt = Double.parseDouble(rAmnNameField.getText().toString());
			balAdd = new BalanceAdd();
			balAdd.execute();
		}
	}
	private class BalanceAdd extends AsyncTask<Void, Void, Boolean> {
		private DBHandler database = new DBHandler(getApplicationContext());
		protected Boolean doInBackground (Void... args) {
			Log.d("Depositing", "Here");
			database.depositBalance(transAmnt, database.selectedAccount, source, date);
			return true;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			balAdd = null;
			
			if (success) {
				Intent i=new Intent(
						AddBalance.this,
						TransactionActivity.class);
				Toast.makeText(getApplicationContext(), "Deposited $" + transAmnt, Toast.LENGTH_LONG).show();
				startActivity(i);
			} else {
				rAmnNameField.setError("Failed to deposit");
				rAmnNameField.requestFocus();
			}
		}
		
		@Override
		protected void onCancelled() {
			balAdd = null;
		}
	}
	

}
