package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PendingActivity extends Activity{
	Button btnShipping;
	Button btnMap;
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
        
        btnMap = (Button)this.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new OnClickListener() {
        	 @Override
  		   public void onClick(View v) {
  		    // TODO Auto-generated method stub
  		
  		           //Intent myIntent = new Intent(PendingActivity.this, HelloGoogleMaps.class);
  		           //PendingActivity.this.startActivity(myIntent);
  		           
        		 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=37.420098,-95.712891&daddr=37.020098,-96.712891+to:37.520098,-96.712891"));
        		 startActivity(browserIntent);
        		 
        		 	//This is not finished because we want to go back
  		           //PendingActivity.this.finish();
  		    
  		   }
  		});
        }
    }


