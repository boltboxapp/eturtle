package edu.turtle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ApiService extends Service {

	
    public class LocalBinder extends Binder {
        ApiService getService() {
            return ApiService.this;
        }
    }

    @Override
    public void onCreate() {
    	
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ApiService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        
        
    	Log.i("ApiService", "Api Service Stopped");
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
    public void testservice(){
    	
    	Log.i("ApiService","Test");
    }
    
}