package edu.turtle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			
			
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.exit)
	        .setMessage(R.string.wantexit)
	        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	                //Stop the activity
	                MainActivity.this.finish();    
	            }

	        })
	        .setNegativeButton(R.string.cancel, null)
	        .show();
		
		
		}

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


