package ext.atos.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Statistics extends Activity{

	private static final String STATISTICS_ACTIVITY = "STATISTICS_ACTIVITY";
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.statistics);
        
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