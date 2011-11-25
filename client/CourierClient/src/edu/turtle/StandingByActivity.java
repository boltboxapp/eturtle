package edu.turtle;

import org.apache.http.client.HttpResponseException;

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
				try {
					boundservice.checkout();
					Toast.makeText(StandingByActivity.this, "Checking out",Toast.LENGTH_LONG).show();  
					Intent myIntent = new Intent(StandingByActivity.this,IdleActivity.class);
			        StandingByActivity.this.startActivity(myIntent);
			        StandingByActivity.this.finish();
					
				} catch (HttpResponseException e) {
					// TODO Auto-generated catch block
					if(e.getStatusCode()==500)
						Toast.makeText(StandingByActivity.this, "Server Error!",Toast.LENGTH_LONG).show();
				}
				catch (Exception e) {
					Toast.makeText(StandingByActivity.this, "Could not connect to server.",Toast.LENGTH_LONG).show();
				}
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
       
       btnCheckOut = (Button)this.findViewById(R.id.btnCheckOut);
       btnCheckOut.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
		    
		           checkout();
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
