package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

		Intent lsintent;
		Intent nsintent;
		Intent asintent;
		

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
	        MainActivity.this.startActivity(myIntent);
	           
	        //Starting location service
	        Log.i("LocationService","Starting location service");
	        lsintent = new Intent(MainActivity.this, LocationService.class);
	        MainActivity.this.startService(lsintent);
	        
	        //Starting notification service
	        Log.i("NotificationService","Starting notification service");
	        nsintent = new Intent(MainActivity.this, NotificationService.class);
	        MainActivity.this.startService(nsintent);
	        
	        //Starting api service
	        Log.i("ApiService","Starting api service");
	        asintent = new Intent(MainActivity.this, ApiService.class);
	        MainActivity.this.startService(asintent);
	        
	        
	        
	    }

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			MainActivity.this.stopService(lsintent);
			MainActivity.this.stopService(nsintent);
			MainActivity.this.stopService(asintent);
			super.onDestroy();
		}
	    
	    
	} 


