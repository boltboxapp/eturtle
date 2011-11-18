package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShippingActivity extends Activity{
	Button btnIdle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shippinglayout);
        btnIdle = (Button)this.findViewById(R.id.btnLogin);
        btnIdle.setOnClickListener(new OnClickListener() {
   
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub

           Toast.makeText(ShippingActivity.this, "No packages",Toast.LENGTH_LONG).show();
           Intent myIntent = new Intent(ShippingActivity.this, IdleActivity.class);
           ShippingActivity.this.startActivity(myIntent);
    
    
   }
  });       
    }

}
