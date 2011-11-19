package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

		Intent newinIntent;
		
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
	        MainActivity.this.startActivity(myIntent);
	           
	        //Starting location service
	        Log.i("LocationService","Starting location service");
	        newinIntent = new Intent(MainActivity.this, LocationService.class);
	        MainActivity.this.startService(newinIntent);
	    }

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			MainActivity.this.stopService(newinIntent);
			super.onDestroy();
		}
	    
	    
	} 


