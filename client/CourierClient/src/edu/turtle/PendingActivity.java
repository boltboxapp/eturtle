package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PendingActivity extends Activity{
	Button btnShipping;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendinglayout);
        btnShipping = (Button)this.findViewById(R.id.btnShipping);
        btnShipping.setOnClickListener(new OnClickListener() {
   
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub

           Toast.makeText(PendingActivity.this, "No packages",Toast.LENGTH_LONG).show();
           Intent myIntent = new Intent(PendingActivity.this, ShippingActivity.class);
           PendingActivity.this.startActivity(myIntent);
           PendingActivity.this.finish();
    
   }
  });       
    }

}
