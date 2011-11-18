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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standingbylayout);
        btnPending = (Button)this.findViewById(R.id.btnPending);
        btnPending.setOnClickListener(new OnClickListener() {
   
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub

           Toast.makeText(StandingByActivity.this, "No packages",Toast.LENGTH_LONG).show();
           Intent myIntent = new Intent(StandingByActivity.this,PendingActivity.class);
           StandingByActivity.this.startActivity(myIntent);
    
    
   }
  });       
    }

}
