package edu.turtle;

import org.apache.http.client.HttpResponseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShippingActivity extends Activity{
	Button btnSuccess;
	Button btnFail;
	ImageButton btnMap;
	
	String name;
	String source;
	String destination;
	String date_created;
	String src_lat;
	String src_lng;
	String dst_lat;
	String dst_lng;
	
	private ApiService boundservice;
	private ServiceConnection sc;
	
	private void api_method(final String method) {
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				try{
					if (method.contentEquals("COMPLETE")) {
						boundservice.complete();
						Toast.makeText(ShippingActivity.this, "Success",Toast.LENGTH_LONG).show();
				        
						}
					else if (method.contentEquals("FAIL")) {
						boundservice.fail();
						Toast.makeText(ShippingActivity.this, "Fail",Toast.LENGTH_LONG).show();
					} 
					Intent myIntent = new Intent(ShippingActivity.this, IdleActivity.class);
			        ShippingActivity.this.startActivity(myIntent);
			        ShippingActivity.this.finish();
				}
				catch (HttpResponseException e){
					if(e.getStatusCode()==500)
						Toast.makeText(ShippingActivity.this, "Server Error!",Toast.LENGTH_LONG).show();				
				}
				catch (Exception e) {
					Toast.makeText(ShippingActivity.this, "Could not connect to server.",Toast.LENGTH_LONG).show();
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
			   new AlertDialog.Builder(ShippingActivity.this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.wantcomplete_title)
		        .setMessage(R.string.wantcomplete)
		        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

		            	api_method("COMPLETE");   
		            }

		        })
		        .setNegativeButton(R.string.cancel, null)
		        .show();
		    
		   }
		  });     
        btnFail = (Button)this.findViewById(R.id.btnFail);
        btnFail.setOnClickListener(new OnClickListener() {
   
		   @Override
		   public void onClick(View v) {
			   new AlertDialog.Builder(ShippingActivity.this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.wantcomplete_title)
		        .setMessage(R.string.wantfail)
		        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

		            	api_method("FAIL");  
		            }

		        })
		        .setNegativeButton(R.string.cancel, null)
		        .show();
		   }
		  });
        
        Bundle bundle = getIntent().getExtras();
        name =bundle.getString("name");
        source = bundle.getString("source");
        destination = bundle.getString("destination");
        date_created = bundle.getString("date_created");
        src_lat = bundle.getString("src_lat");
        src_lng = bundle.getString("src_lng");
        dst_lat = bundle.getString("dst_lat");
        dst_lng = bundle.getString("dst_lng");
        
        TextView pkgname = (TextView)this.findViewById(R.id.pkgname);
        TextView srcaddress = (TextView)this.findViewById(R.id.srcaddress);
        TextView dstaddress = (TextView)this.findViewById(R.id.dstaddress);
        TextView datecreated = (TextView)this.findViewById(R.id.datecreated);
       	
		pkgname.setText(name);
		srcaddress.setText(source);
        dstaddress.setText(destination);
        datecreated.setText(date_created);
        
        
        btnMap = (ImageButton)this.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+src_lat+","+src_lng+"&daddr="+dst_lat+","+dst_lng));
        		 startActivity(browserIntent);
        		 
        		 //This is not finished because we want to go back
  		         //PendingActivity.this.finish();
  		   }
  		});
    }

	@Override
	protected void onDestroy() {
		if (sc != null){
			unbindService(sc);}
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
