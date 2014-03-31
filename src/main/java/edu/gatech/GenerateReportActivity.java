package main.java.edu.gatech;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class GenerateReportActivity extends Activity {
	
	
	Button btnSelectBeginDate;
	Button btnSelectEndDate;
	static final int DATE1_DIALOG_ID = 0; 
	static final int DATE2_DIALOG_ID = 1;
	public int yearSelected, monthSelected, daySelected; 
    public int year2, month2, day2;
    public String startDate, endDate;
    private int mYear, mMonth, mDay; 

    
    public GenerateReportActivity()
    {
         
    			final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                
    }
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate_report);
		btnSelectBeginDate=(Button)findViewById(R.id.buttonSelectBeginDate); 
		btnSelectEndDate= (Button)findViewById(R.id.buttonSelectEndDate);
		Button btn = (Button) findViewById(R.id.button_return);
		btnSelectBeginDate.setOnClickListener(new View.OnClickListener() {
             
             @SuppressWarnings("deprecation")
			public void onClick(View v) {
                 // Show the DatePickerDialog
                  showDialog(DATE1_DIALOG_ID);
             }
         });
	    
		btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                // Show the TimePickerDialog
                 showDialog(DATE2_DIALOG_ID);
            }
        });
		
        btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("clicks", "clicked return button");
				Intent i = new Intent(
						GenerateReportActivity.this,
						MainMenu.class);
				startActivity(i);
			}
		});
        
        Button gbtn = (Button)findViewById(R.id.button1);
        gbtn.setOnClickListener(new View.OnClickListener() {
            
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
            	String report = generateReport(startDate, endDate);
            	Intent i = new Intent(
						GenerateReportActivity.this,
						ReportPageActivity.class);
            	i.putExtra("Report", report);
				startActivity(i);
            }
        });
	}
	
	 private DatePickerDialog.OnDateSetListener mDateSetListener =
             new DatePickerDialog.OnDateSetListener() {
         // the callback received when the user "sets" the Date in the DatePickerDialog
                 public void onDateSet(DatePicker view, int yearSelected,
                                       int monthOfYear, int dayOfMonth) {
                    // Set the Selected Date in Select date Button
                    btnSelectBeginDate.setText("Date selected : "+dayOfMonth+"-"+monthOfYear+"-"+yearSelected);                   
                    startDate = Utils.getDate(monthOfYear, yearSelected, dayOfMonth);
                    Log.d("startdate", "Setting startdate = " + startDate);
                 }
             };

             // Register  TimePickerDialog listener                 
             private DatePickerDialog.OnDateSetListener mDate2SetListener =
                 new DatePickerDialog.OnDateSetListener() {
             // the callback received when the user "sets" the TimePickerDialog in the dialog
                     public void onDateSet(DatePicker view, int yearSelected,
                             int monthOfYear, int dayOfMonth) {
                         // Set the Selected Date in Select date Button
                         btnSelectEndDate.setText("End Date selected :"+dayOfMonth+"-"+monthOfYear+"-"+yearSelected);
                         endDate = Utils.getDate(monthOfYear, yearSelected, dayOfMonth);
                         Log.d("enddate", "Setting enddate = " + endDate);
                       }
                 };

             // Method automatically gets Called when you call showDialog()  method
             @Override
             protected Dialog onCreateDialog(int id) {
                     switch (id) {
                     case DATE1_DIALOG_ID:
              // create a new DatePickerDialog with values you want to show 
                         return new DatePickerDialog(this,
                                     mDateSetListener,
                                     mYear, mMonth, mDay);
             // create a new DatePickerDialog with values you want to show 
                     case DATE2_DIALOG_ID:
                    	 return new DatePickerDialog(this,
                                 mDate2SetListener,
                                 mYear, mMonth, mDay);
                    
                     }
                     return null;
                 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generate_report, menu);
		return true;
	}
	
	private String generateReport(String startDate, String endDate){		
		DBHandler database = new DBHandler(getApplicationContext());
		String report = database.generateSpendingCategoriesReport(startDate, endDate);
		return report;
	}
	
	
}
