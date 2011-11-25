package edu.turtle;

import java.io.IOException;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {

	LocationManager locationManager;
	LocationListener locationListener;
	
	private ApiService boundservice;
	private ServiceConnection sc;
	
	double longitude, latitude;
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
    	
    	//create api_service call
    	sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("LocationService","Connected to Api Service");
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(LocationService.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
    	
    	// Acquire a reference to the system Location Manager
    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    	// Define a listener that responds to location updates
    	locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      longitude = location.getLongitude();
    	      latitude = location.getLatitude();
    	    	Log.i("LocationService", "Updated location long: "+ longitude + "  lat: "+ latitude);
    	    	if (boundservice != null){
    	    	try {
					boundservice.update_location(longitude, latitude);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(LocationService.this, "Could not connect to server",Toast.LENGTH_LONG).show();
		        	
				}}
    	    	
    	    }

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    
    	
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocationService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        
        // Tell the user we stopped.
    	locationManager.removeUpdates(locationListener);
    	Log.i("LocationService", "Location Service Stopped");
        Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show();
       
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