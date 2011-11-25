package edu.turtle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ApiService extends Service {

	private List<Cookie> cookies = null;
	DefaultHttpClient httpclient;
	private  String APIURL = "http://lepi.zapto.org/api/";
	private String c2dmregid;
	
    public class LocalBinder extends Binder {
        ApiService getService() {
            return ApiService.this;
        }
    }

    @Override
    public void onCreate() {
    	HttpParams httpParameters = new BasicHttpParams();
    	// Set the timeout in milliseconds until a connection is established.
    	int timeoutConnection = 3000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	// Set the default socket timeout (SO_TIMEOUT) 
    	// in milliseconds which is the timeout for waiting for data.
    	int timeoutSocket = 5000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	httpclient = new DefaultHttpClient(httpParameters);
    	

    	
    	SharedPreferences settings = getSharedPreferences("ETurtlePreferences", 0);
    	APIURL = settings.getString("apiurl", "http://lepi.zapto.org/api/");
    	 
    	
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
    public int login(String username, String password){
    	
    	Log.i("ApiService","Logging in");
    	       
        HttpPost httpost = new HttpPost(APIURL + "login/");
        Log.i("ApiService",APIURL);
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
		if (response==null) return 0;
		if (response.getStatusLine().getStatusCode()!=200)
    	{
    		return response.getStatusLine().getStatusCode();    		
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
        
       Log.i("ApiService","Attempting c2dm key posting with saved key");
       update_c2dm_key(); 
       return 200;
    	
    }
    public void checkin(){    	
    	httpget(APIURL+"check_in/");    	
    }
    
    public void checkout(){    	
    	httpget(APIURL+"leave/");    	
    }
    public void accept(){    	
    	httpget(APIURL+"accept/");    	
    }
    
    public void decline(){    	
    	httpget(APIURL+"decline/");    	
    }
    
    public void complete(){    	
    	httpget(APIURL+"complete/");    	
    }
    
    public void fail(){    	
    	httpget(APIURL+"fail/");    	
    }
    
    public void get(){    	
    	Log.i("ApiService",httpget(APIURL+"get/"));    	
    }
    
    
    public void update_location(double longitude, double latitude)
    {
    	if (cookies!=null){
	    	ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("lng", String.valueOf(longitude)));
	        nvps.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
	    	httppost(APIURL+"loc_update/",nvps);
    	}
    }
    public void update_c2dm_key(String key)
    {
    	if (cookies!=null){
	    	ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("registration_id", key));
	        
	    	httppost(APIURL+"c2dmkey_update/",nvps);
    	} else {
    		
    		c2dmregid = key;
    	}
    }
    public void update_c2dm_key()
    {
    	if (c2dmregid != null) 
    	{
    		update_c2dm_key(c2dmregid);
    		
    	}
    }
    
    private String httpget(String url){
    	
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
        
        HttpEntity entity = response2.getEntity();
        InputStream inputStream = null;
		try {
			inputStream = entity.getContent();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Read response into a buffered stream
        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        try {
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
			    content.write(sBuffer, 0, readBytes);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if (entity2 != null) {
            try {
				entity2.consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }    	
    	return new String(content.toByteArray());
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