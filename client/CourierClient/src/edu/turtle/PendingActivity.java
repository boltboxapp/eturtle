package edu.turtle;

import org.apache.http.client.HttpResponseException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PendingActivity extends Activity{
	Button btnAccept;
	Button btnReject;
	Button btnCheckOut;
	Button btnMap;
	
	private ApiService boundservice;
	private ServiceConnection sc;
	
	
	private void api_method(final String method) {
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				try{
					if (method.contentEquals("ACCEPT")) {
						boundservice.accept();
						Toast.makeText(PendingActivity.this, "Accepting",Toast.LENGTH_LONG).show();
				        Intent myIntent = new Intent(PendingActivity.this, ShippingActivity.class);
				        PendingActivity.this.startActivity(myIntent);
				        PendingActivity.this.finish();
						}
					else if (method.contentEquals("DECLINE")) {
						boundservice.decline();
						Toast.makeText(PendingActivity.this, "Rejecting",Toast.LENGTH_LONG).show();				        
						Intent myIntent = new Intent(PendingActivity.this, StandingByActivity.class);
				        PendingActivity.this.startActivity(myIntent);
				        PendingActivity.this.finish();
						
					} else if (method.contentEquals("LEAVE")){
						
						boundservice.checkout();
						Toast.makeText(PendingActivity.this, "Rejecting and Checking out",Toast.LENGTH_LONG).show();
		 		        Intent myIntent = new Intent(PendingActivity.this,IdleActivity.class);
		 		        PendingActivity.this.startActivity(myIntent);
		 		        PendingActivity.this.finish();
					}
				}
				catch (HttpResponseException e){
					if(e.getStatusCode()==500)
						Toast.makeText(PendingActivity.this, "Server Error!",Toast.LENGTH_LONG).show();
						
				}
				catch (Exception e) {
					Toast.makeText(PendingActivity.this, "Could not connect to server.",Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(PendingActivity.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendinglayout);
        
        
        
        String regid= getIntent().getStringExtra("message");    
        Log.i("RegistrationService", "PENDING "+regid);
        
        
        TextView pkgname = (TextView)this.findViewById(R.id.pkgname);
        TextView srcaddress = (TextView)this.findViewById(R.id.srcaddress);
        TextView dstaddress = (TextView)this.findViewById(R.id.dstaddress);
        TextView datecreated = (TextView)this.findViewById(R.id.datecreated);
       
        pkgname.setText("alma");
        srcaddress.setText("korta");
        dstaddress.setText("korta");
        datecreated.setText("korta");
        
        btnAccept = (Button)this.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new OnClickListener() {    
		   @Override
		   public void onClick(View v) {
		           
		           api_method("ACCEPT");
		    
		   }
		});
        btnReject = (Button)this.findViewById(R.id.btnReject);
        btnReject.setOnClickListener(new OnClickListener() {    
		   @Override
		   public void onClick(View v) {
		          
	           api_method("DECLINE");
	           
	           
		    
		   }
		});
        btnMap = (Button)this.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new OnClickListener() {
        	 @Override
  		   public void onClick(View v) {
  		    // TODO Auto-generated method stub
  		
  		          
        		 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=37.420098,-95.712891&daddr=37.020098,-96.712891+to:37.520098,-96.712891"));
        		 startActivity(browserIntent);
        		 
        		 //This is not finished because we want to go back
  		         //PendingActivity.this.finish();
  		    
  		   }
  		});
        
        btnCheckOut = (Button)this.findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(new OnClickListener() {
    
 		   @Override
 		   public void onClick(View v) {
 		           
 		           api_method("LEAVE");
 		           
 		           
 		    
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


