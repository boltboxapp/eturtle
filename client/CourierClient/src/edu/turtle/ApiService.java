package edu.turtle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ApiService extends Service {

	private List<Cookie> cookies = null;
	DefaultHttpClient httpclient;
	private  String BACKEND = "http://lepi.zapto.org/api/";
    public class LocalBinder extends Binder {
        ApiService getService() {
            return ApiService.this;
        }
    }

    @Override
    public void onCreate() {
    	httpclient = new DefaultHttpClient();
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
        
    	httpclient.getConnectionManager().shutdown();   
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
    public void login(String username, String password){
    	
    	Log.i("ApiService","Logging in");
    	       
        HttpPost httpost = new HttpPost(BACKEND + "login/");
        
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "roka"));
        nvps.add(new BasicNameValuePair("password", "roka"));

        try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpEntity entity = response.getEntity();

        Log.i("ApiService","Login form get: " + response.getStatusLine());
    	
        if (entity != null) {
            try {
				entity.consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        cookies = httpclient.getCookieStore().getCookies();
        httpclient.getCookieStore().addCookie(cookies.get(0));
        if (cookies.isEmpty()) {
            
        } else {
            for (int i = 0; i < cookies.size(); i++) {
            	Log.i("ApiService","- " + cookies.get(i).toString());
            }
        }
        
       
        
    
    	
    }
    public void checkin(){    	
    	httpget(BACKEND+"check_in/");    	
    }
    
    public void checkout(){    	
    	httpget(BACKEND+"leave/");    	
    }
    public void accept(){    	
    	httpget(BACKEND+"accept/");    	
    }
    
    public void decline(){    	
    	httpget(BACKEND+"decline/");    	
    }
    
    public void complete(){    	
    	httpget(BACKEND+"complete/");    	
    }
    
    public void fail(){    	
    	httpget(BACKEND+"fail/");    	
    }
    
    public void update_location(double longitude, double latitude)
    {
    	if (cookies!=null){
	    	ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("lng", String.valueOf(longitude)));
	        nvps.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
	    	httppost(BACKEND+"loc_update/",nvps);
    	}
    }
    
    private void httpget(String url){
    	
    	HttpGet httpget = new HttpGet(url);
        
        
        HttpResponse response2 = null;
		try {
			response2 = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpEntity entity2 = response2.getEntity();
        
        Log.i("ApiService",url + " " + response2.getStatusLine());
        if (entity2 != null) {
            try {
				entity2.consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }    	
    	
    }
    private void httppost(String url, ArrayList <NameValuePair> params){
    	
    	   
        HttpPost httpost = new HttpPost(url);
        


        try {
			httpost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpEntity entity = response.getEntity();

        Log.i("ApiService","Location update form get: " + response.getStatusLine());
    	
        if (entity != null) {
            try {
				entity.consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
            	
    }
}