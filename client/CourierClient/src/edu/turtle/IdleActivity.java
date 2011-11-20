package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IdleActivity extends Activity{
	Button btnCheckIn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idlelayout);
        btnCheckIn = (Button)this.findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new OnClickListener() {
   
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub

           Toast.makeText(IdleActivity.this, "Checking in...",Toast.LENGTH_LONG).show();
           Intent myIntent = new Intent(IdleActivity.this, StandingByActivity.class);
           IdleActivity.this.startActivity(myIntent);
           IdleActivity.this.finish();
           
    
   }
  });       
    }

}
