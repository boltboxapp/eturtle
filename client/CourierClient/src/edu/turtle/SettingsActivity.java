package edu.turtle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class SettingsActivity extends Activity{
	public static final String PREFS_NAME = "ETurtlePreferences";
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String apiurl = settings.getString("apiurl", "http://lepi.zapto.org");
        ((EditText)findViewById(R.id.settings_apiurl)).setText(apiurl);
    }
	
	@Override
    protected void onStop(){
       super.onStop();

      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      String apiurl = ((EditText)findViewById(R.id.settings_apiurl)).getText().toString();
      editor.putString("apiurl",apiurl);
      editor.commit();
    }
}
