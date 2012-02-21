package ext.atos.mobile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Overview extends Activity{

	private static final String OVERVIEW_ACTIVITY = "OVERVIEW_ACTIVITY";
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.overview);
        
        DbHelper dbHelper = new DbHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor results = db.query("SERVICES",
			  new String[]{"SERVICE_STARTED"},
			  "SERVICE_NAME = '" + SmsService.SMS_SERVICE + "'",
			  null, null, null, null
		);
		
		boolean serviceRunning = false;
		
		while(results.moveToNext()){
			int running = results.getInt(0);
			if(running == 1){
				serviceRunning = true;
			}
		}
		
		TextView text = (TextView)findViewById(R.id.overview_text);
		if(serviceRunning){
			text.setText(getString(R.string.overview_service_started_label));
		}else{
			text.setText(getString(R.string.overview_service_stopped_label));
		}
		
		results.close();
		db.close();
		dbHelper.close();
        
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
    
}