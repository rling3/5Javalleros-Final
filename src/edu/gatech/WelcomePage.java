package edu.gatech;

import main.java.edu.gatech.LoginActivity;
import main.java.edu.gatech.R;
import main.java.edu.gatech.RegisterActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.DialogInterface.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class WelcomePage extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Button btn1 = (Button) findViewById(R.id.button_register);
        btn1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Log.i("clicks","You Clicked Login");
                Intent i=new Intent(
                       WelcomePage.this,
                       LoginActivity.class);
                startActivity(i);
        	}
        });
        Button btn2 = (Button) findViewById(R.id.Button01);
        btn2.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Log.i("clicks","You clicked Register");
        		Intent i = new Intent (
        				WelcomePage.this,
        				RegisterActivity.class);
        		startActivity(i);
        	}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_page, menu);
        return true;
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
    
}
