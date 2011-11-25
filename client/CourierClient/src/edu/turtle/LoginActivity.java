package edu.turtle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
 
 
 EditText txtUserName;
 EditText txtPassword;
 Button btnLogin;
 Button btnCancel;
 CheckBox chkRemember;
 
 private ApiService boundservice;
 private ServiceConnection sc;
 
 	private boolean authenticate(final String username, final String password) {
 		
 		 sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				boundservice = ((ApiService.LocalBinder)service).getService();
				Log.i("ApiService","Connected to Api Service");
				int loginstatus = boundservice.login(username, password);
				if( loginstatus == 200)
				{	
		           Intent myIntent = new Intent(LoginActivity.this, IdleActivity.class);
		           LoginActivity.this.startActivity(myIntent);
		           LoginActivity.this.finish();
		    	} else if (loginstatus == 401) {
		           Toast.makeText(LoginActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
		        } else {
		        	Toast.makeText(LoginActivity.this, "Could not connect to login server",Toast.LENGTH_LONG).show();
		        	
		        }
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				boundservice = null;
			}};
		bindService(new Intent(LoginActivity.this, ApiService.class), sc, Context.BIND_AUTO_CREATE);
		
		return true;
	}    
 	/** Called when the activity is first created. */
 	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        SharedPreferences settings = getSharedPreferences("ETurtlePreferences", 0);
        
        txtUserName=(EditText)this.findViewById(R.id.txtUname);
        txtPassword=(EditText)this.findViewById(R.id.txtPwd);
        txtUserName.setText(settings.getString("username", ""));
        txtPassword.setText(settings.getString("password", ""));
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   
		       SharedPreferences.Editor editor = getSharedPreferences("ETurtlePreferences", 0).edit();
		       Boolean remember_me = chkRemember.isChecked();
		       editor.putBoolean("remember_me",remember_me);
		       editor.commit();
			   
			   if (chkRemember.isChecked()==true){
				   editor.putString("username", txtUserName.getText().toString());
				   editor.putString("password", txtPassword.getText().toString());
			   } else {
				   editor.remove("username");
				   editor.remove("password");
			   }
			   
		       editor.commit();
			   
			   authenticate(txtUserName.getText().toString(), txtPassword.getText().toString());
		   }
        }); 
        chkRemember=(CheckBox)this.findViewById(R.id.chkRemember_me);
        Boolean remember_me = settings.getBoolean("remember_me", false);
        chkRemember.setChecked(remember_me);
    }

 	@Override
	protected void onDestroy() {
 		if (sc!=null){
 			unbindService(sc);
 		}
 		super.onDestroy();
	}
 	
 	//Options Menu create
 	@Override
 	public boolean onCreateOptionsMenu(Menu menu){
 		MenuInflater inflater = getMenuInflater();
 	    inflater.inflate(R.menu.login_menu, menu);
 	    return true;
 	}
 	
 	//Menu events handle
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 	    // Handle item selection
 	    switch (item.getItemId()) {
 	    case R.id.settings:
 	    	Intent intent=new Intent(this, SettingsActivity.class);
            startActivity(intent);
 	        return true;
 	    case R.id.exit:
 	        this.finish();
 	        return true;
 	    default:
 	        return super.onOptionsItemSelected(item);
 	    }
 	}
} 
