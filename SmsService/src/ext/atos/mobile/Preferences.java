package ext.atos.mobile;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

public class Preferences extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener, android.widget.RadioGroup.OnCheckedChangeListener {

	private static final String PREFERENCE_ACTIVITY = "PREFERENCE_ACTIVITY";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	Log.d(PREFERENCE_ACTIVITY, "Preferences.onCreate(Bundle) BEGIN");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        
        //Find if service is already running
        DbHelper dbHelper = new DbHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor results = db.query("SERVICES",
			  new String[]{"SERVICE_STARTED"},
			  "SERVICE_NAME = '" + SmsService.SMS_SERVICE + "'",
			  null, null, null, null
		);
		Log.d(PREFERENCE_ACTIVITY, results.getCount() + " results found.");
		boolean serviceRunning = false;
		while(results.moveToNext()){
			int running = results.getInt(0);
			Log.d(PREFERENCE_ACTIVITY, "Running? " + running);
			if(running == 1){
				serviceRunning = true;
			}
		}
		results.close();
		
		results = db.query("PREFERENCES",
				  new String[]{"VALUE"},
				  "NAME = 'CONNECTION_TYPE'",
				  null, null, null, null
		);
		String connectionType = null;
		while(results.moveToNext()){
			connectionType = results.getString(0);
		}
		results.close();
		
		db.close();
		dbHelper.close();
        
		//Set default checkbox value based on the service running boolean retrieved from DB
        CheckBox enableService = (CheckBox)findViewById(R.id.enableService);
        enableService.setChecked(serviceRunning);
        Log.d(PREFERENCE_ACTIVITY, "Register the listener...");
        enableService.setOnCheckedChangeListener(this);
        
        RadioGroup group = (RadioGroup)findViewById(R.id.connectionType);
        if(connectionType != null){
        	group.check(Integer.parseInt(connectionType));
        }
        group.setOnCheckedChangeListener(this);
        
        Log.d(PREFERENCE_ACTIVITY, "Preferences.onCreate(Bundle) END");
        
    }
    
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	
    	Intent invokeSmsService = new Intent(this, SmsService.class);
    	if(isChecked){
    		ComponentName name = startService(invokeSmsService);
    		Log.i(PREFERENCE_ACTIVITY, "Sms service started? " + (name != null ? "true": "false"));
    	}else{
    		//Disable service
    		boolean serviceStopped = stopService(invokeSmsService);
    		Log.i(PREFERENCE_ACTIVITY, "Sms service stopped? " + serviceStopped);
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sms_main_menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.overview:
            startOverviewActivity();
            return true;
        case R.id.preferences:
            startPreferencesActivity();
            return true;
        case R.id.statistics:
        	startStatisticsActivity();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    
    private void startOverviewActivity(){
    	Intent overviewActivity = new Intent(this, Overview.class);
    	startActivity(overviewActivity);
    }
    
    
    private void startPreferencesActivity(){
    	Intent preferenceActivity = new Intent(this, Preferences.class);
    	startActivity(preferenceActivity);
    }
    
    
    private void startStatisticsActivity(){
    	Intent statisticsActivity = new Intent(this, Statistics.class);
    	startActivity(statisticsActivity);
    }

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		DbHelper dbHelper = new DbHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("VALUE", checkedId);
		db.update("PREFERENCES", values, "NAME='CONNECTION_TYPE'" , null);
		db.close();
		dbHelper.close();
		
	}
    
}