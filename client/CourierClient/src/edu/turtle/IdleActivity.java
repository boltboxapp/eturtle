package edu.turtle;

import java.io.IOException;

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

public class IdleActivity extends Activity{
	Button btnCheckIn;
	private ApiService boundservice;
	

	private ServiceConnection sc;
	 
	private void checkin() {
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				try {
					boundservice.checkin();
					Toast.makeText(IdleActivity.this, "Checking in...",Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(IdleActivity.this, StandingByActivity.class);
			        IdleActivity.this.startActivity(myIntent);
			        IdleActivity.this.finish();
				} catch (HttpResponseException e) {
					if(e.getStatusCode()==500)
						Toast.makeText(IdleActivity.this, "Server Error!",Toast.LENGTH_LONG).show();
				}
				catch (IOException e) {
					Toast.makeText(IdleActivity.this, "Something went wrong.",Toast.LENGTH_LONG).show();
				}
				
				
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(IdleActivity.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idlelayout);
        btnCheckIn = (Button)this.findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new OnClickListener() {
   
    
        	
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub

           
           
           checkin();
           
           
           
    
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
