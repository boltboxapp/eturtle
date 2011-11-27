package edu.turtle;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PendingActivity extends Activity{
	Button btnAccept;
	JSONObject messagejson = null;
	
	Notification notification;
	
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
				        
				        myIntent.putExtra("name", messagejson.getString("name"));
				        myIntent.putExtra("source", messagejson.getString("source"));
				        myIntent.putExtra("destination", messagejson.getString("destination"));
				        myIntent.putExtra("date_created", messagejson.getString("date_created"));
				        myIntent.putExtra("src_lat", messagejson.getString("src_lat"));
				        myIntent.putExtra("src_lng", messagejson.getString("src_lng"));
				        myIntent.putExtra("dst_lat", messagejson.getString("dst_lat"));
				        myIntent.putExtra("dst_lng", messagejson.getString("dst_lng"));
				        
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
        
        try {
    	
	    	String message= getIntent().getStringExtra("message");
	    	if (message.equals("TIMEOUT"))
	        	PendingActivity.this.finish();
	        Log.i("RegistrationService", "PENDING "+message);
	        
	        try {
				messagejson = new JSONObject(message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	        
	        TextView pkgname = (TextView)this.findViewById(R.id.pkgname);
	        TextView srcaddress = (TextView)this.findViewById(R.id.srcaddress);
	        TextView dstaddress = (TextView)this.findViewById(R.id.dstaddress);
	        TextView datecreated = (TextView)this.findViewById(R.id.datecreated);
	       	
			pkgname.setText(messagejson.getString("name"));
			srcaddress.setText(messagejson.getString("source"));
	        dstaddress.setText(messagejson.getString("destination"));
	        datecreated.setText(messagejson.getString("date_created"));
	        
//	        final String srclat = messagejson.getString("src_lat");
//	        final String srclong = messagejson.getString("src_lng");
//	        final String dstlat = messagejson.getString("dst_lat");
//	        final String dstlong = messagejson.getString("dst_lng");
	        
	        if (notification==null)
	        	showNotification(message, messagejson.getString("name"), messagejson.getString("source"), messagejson.getString("destination"));
	        
	        btnAccept = (Button)this.findViewById(R.id.btnAccept);
	        btnAccept.setOnClickListener(new OnClickListener() {    
			   @Override
			   public void onClick(View v) {	           
				   api_method("ACCEPT");
				   cancelNotification();
			   }
			});
        
		}catch (JSONException e) {
			e.printStackTrace();
		}
        
    }
	
	@Override
	protected void onDestroy() {
		if (sc != null){
			unbindService(sc);}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.pending_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ment_decline:
	    	
	    	//---alert
	    	new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.wantdecline_title)
	        .setMessage(R.string.wantdecline)
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	            	api_method("DECLINE");
	    	    	cancelNotification();   
	            }

	        })
	        .setNegativeButton(R.string.cancel, null)
	        .show();
	    	//------
	    	
	        return true;
	    case R.id.menu_leave:
	    	
	    	//---alert
	    	new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.wantdecline_title)
	        .setMessage(R.string.wantdecline)
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	            	api_method("LEAVE");
	    	    	cancelNotification(); 
	            }

	        })
	        .setNegativeButton(R.string.cancel, null)
	        .show();
	    	//------
	    	
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.wantdecline_title)
        .setMessage(R.string.wantdecline)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            	api_method("LEAVE");
    	    	cancelNotification();   
            }

        })
        .setNegativeButton(R.string.cancel, null)
        .show();
	
	
	}
	
	void showNotification(String msg,String pkg_name, String pkg_src, String pkg_dst){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.stat_sys_warning;
		CharSequence tickerText = getString(R.string.notification_title);
		long when = System.currentTimeMillis();

		notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_INSISTENT;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		
		
		Context context = getApplicationContext();
		CharSequence contentTitle = getString(R.string.notification_title);
		CharSequence contentText = pkg_name + " (" + pkg_src + " - " + pkg_dst+ ")";
		Intent notificationIntent = new Intent(this, PendingActivity.class);
		notificationIntent.putExtra("message", msg);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(666, notification);
	}
	void cancelNotification(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(666);
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
		String message= intent.getStringExtra("message");
		
		if (message.equals("TIMEOUT")){
			Intent myIntent = new Intent(PendingActivity.this,IdleActivity.class);
			myIntent.putExtra("timed_out", true);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
	        PendingActivity.this.startActivity(myIntent);
	        PendingActivity.this.finish();
		}
		
	    super.onNewIntent(intent);
	} // End of onNewIntent(Intent intent)
	
}


