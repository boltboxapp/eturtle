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

public class StandingByActivity extends Activity{
	Button btnPending;
	Button btnCheckOut;
	
	private ApiService boundservice=null;
	private ServiceConnection sc=null;
	
	
	
	
	private void checkout() {
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				boundservice.checkout();
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(StandingByActivity.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.standingbylayout);
       btnPending = (Button)this.findViewById(R.id.btnPending);
       btnPending.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(StandingByActivity.this, "Testing package recieved",Toast.LENGTH_LONG).show();
		           Intent myIntent = new Intent(StandingByActivity.this,PendingActivity.class);
		           StandingByActivity.this.startActivity(myIntent);
		           StandingByActivity.this.finish();
		    
		   }
		  });       
       btnCheckOut = (Button)this.findViewById(R.id.btnCheckOut);
       btnCheckOut.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    // TODO Auto-generated method stub
		
		           Toast.makeText(StandingByActivity.this, "Checking out",Toast.LENGTH_LONG).show();
		           
		           checkout();
		           
		           Intent myIntent = new Intent(StandingByActivity.this,IdleActivity.class);
		           StandingByActivity.this.startActivity(myIntent);
		           StandingByActivity.this.finish();
		    
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
