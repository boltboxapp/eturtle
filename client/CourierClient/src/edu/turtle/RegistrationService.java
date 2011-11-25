package edu.turtle;

import java.io.IOException;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RegistrationService extends Service {

	private ApiService boundservice;
	private ServiceConnection sc;
	
    public class LocalBinder extends Binder {
        RegistrationService getService() {
            return RegistrationService.this;
        }
    }

    @Override
    public void onCreate() {
    	//Register C2DM
    	
    
    	
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RegistrationService", "Received start id " + startId + ": " + intent);
        Bundle bundle = intent.getBundleExtra("messageBundle");
        final String regid = bundle.getString("registration_id");
        Log.i("RegistrationService", regid);
        
        sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				try {
					boundservice.update_c2dm_key(regid);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(RegistrationService.this, "Could not connect registration service",Toast.LENGTH_LONG).show();
		        	
				}
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(RegistrationService.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
		
		
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        
        
        if (sc != null){
    		unbindService(sc);}
    
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */

}