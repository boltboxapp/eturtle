package edu.turtle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ShippingActivity extends Activity{
	Button btnSuccess;
	Button btnFail;
	
	private ApiService boundservice;
	private ServiceConnection sc;
	
	private void api_method(final String method) {
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				if (method.contentEquals("COMPLETE")) {
					boundservice.complete();
					}
				else if (method.contentEquals("FAIL")) {
					boundservice.fail();
					
				} 
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(ShippingActivity.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
	}
	
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
		           
		           api_method("COMPLETE");
		           
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
		           
		           api_method("FAIL");
		           
		           Intent myIntent = new Intent(ShippingActivity.this, IdleActivity.class);
		           ShippingActivity.this.startActivity(myIntent);
		           ShippingActivity.this.finish();
		    
		   }
		  });     
    }

	@Override
	protected void onDestroy() {
		if (sc != null){
			unbindService(sc);}
		super.onDestroy();
	}
}
