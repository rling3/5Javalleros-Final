package main.java.edu.gatech;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TransactionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		DBHandler database = new DBHandler(getApplicationContext());
		Button addBut = (Button) findViewById(R.id.addBalance);
		Button subtractBut = (Button) findViewById(R.id.subtractBalance);
		Button cancelBut = (Button) findViewById(R.id.cancelBut2);
		addBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked return button");
				Intent i = new Intent(TransactionActivity.this, AddBalance.class);
				startActivity(i);
			}
		});
		subtractBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked return button");
				Intent i = new Intent(TransactionActivity.this, SubtractBalance.class);
				startActivity(i);
			}
		});
		cancelBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "Clicked return button");
				Intent i = new Intent(TransactionActivity.this, AccountsPageActivity.class);
				startActivity(i);
			}
		});
		TextView accountView = (TextView) findViewById(R.id.accountView2);
		accountView.setText("Account: "+database.selectedAccount);
		double balance = database.getBalance(database.selectedAccount);
		TextView balanceView = (TextView) findViewById(R.id.balance);
		balanceView.setText("Balance: $"+balance);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction, menu);
		return true;
	}
}
