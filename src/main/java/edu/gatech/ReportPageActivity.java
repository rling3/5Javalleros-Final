package main.java.edu.gatech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportPageActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_page);
		DBHandler database = new DBHandler(getApplicationContext());
		TextView report = (TextView)findViewById(R.id.textView1);
		Bundle extras = getIntent().getExtras();		
		report.setText(extras.getString("Report"));
		Button done = (Button)findViewById(R.id.button1);
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ReportPageActivity.this, GenerateReportActivity.class);
				startActivity(i);				
			}
		});
		
		
	}
}
