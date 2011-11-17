package futar.pushtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        RegisterC2DM();
    }
    
    private void RegisterC2DM(){
    	Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
        registrationIntent.putExtra("sender", "gigaboleny@gmail.com");
        startService(registrationIntent);
        Log.v("REG","eeeeeee");
    }
    
    private void unRegisterC2DM(){
    	Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
    	unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	startService(unregIntent);
    } 
    
}