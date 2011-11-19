package edu.turtle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
 
 EditText txtUserName;
 EditText txtPassword;
 Button btnLogin;
 Button btnCancel;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        txtUserName=(EditText)this.findViewById(R.id.txtUname);
        txtPassword=(EditText)this.findViewById(R.id.txtPwd);
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
   
   @Override
   public void onClick(View v) {
    // TODO Auto-generated method stub
    
    if((txtUserName.getText().toString()).equals(txtPassword.getText().toString())){
           Toast.makeText(LoginActivity.this, "Login Successful",Toast.LENGTH_LONG).show();
           Intent myIntent = new Intent(LoginActivity.this, IdleActivity.class);
           LoginActivity.this.startActivity(myIntent);
           LoginActivity.this.finish();
    	} else{
           Toast.makeText(LoginActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
          }
    
   }
  });       
    }
} 
