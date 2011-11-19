package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StandingByActivity extends Activity{
	Button btnPending;
	Button btnCheckOut;
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.standingbylayout);
       btnPending = (Button)this.findViewById(R.id.btnPending);
       btnPending.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(StandingByActivity.this, "Testing package recieved",Toast.LENGTH_LONG).show();
		           Intent myIntent = new Intent(StandingByActivity.this,PendingActivity.class);
		           StandingByActivity.this.startActivity(myIntent);
		           StandingByActivity.this.finish();
		    
		   }
		  });       
       btnCheckOut = (Button)this.findViewById(R.id.btnCheckOut);
       btnCheckOut.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(StandingByActivity.this, "Checking out",Toast.LENGTH_LONG).show();
		           Intent myIntent = new Intent(StandingByActivity.this,IdleActivity.class);
		           StandingByActivity.this.startActivity(myIntent);
		           StandingByActivity.this.finish();
		    
		   }
		  });       
    }

}
