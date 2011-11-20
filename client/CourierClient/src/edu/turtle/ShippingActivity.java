package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ShippingActivity extends Activity{
	Button btnSuccess;
	Button btnFail;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shippinglayout);
        btnSuccess = (Button)this.findViewById(R.id.btnSuccess);
        btnSuccess.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(ShippingActivity.this, "Success",Toast.LENGTH_LONG).show();
		           Intent myIntent = new Intent(ShippingActivity.this, IdleActivity.class);
		           ShippingActivity.this.startActivity(myIntent);
		           ShippingActivity.this.finish();
		    
		   }
		  });     
        btnFail = (Button)this.findViewById(R.id.btnFail);
        btnFail.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(ShippingActivity.this, "Fail",Toast.LENGTH_LONG).show();
		           Intent myIntent = new Intent(ShippingActivity.this, IdleActivity.class);
		           ShippingActivity.this.startActivity(myIntent);
		           ShippingActivity.this.finish();
		    
		   }
		  });     
    }

}
