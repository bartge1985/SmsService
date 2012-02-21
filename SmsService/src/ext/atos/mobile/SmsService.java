package ext.atos.mobile;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class SmsService extends Service {

	public static final String SMS_SERVICE = "SMS_SERVICE";
	private SQLiteDatabase db = null;
	private WifiConnection wifiConnection = null;
	private Thread connectionThread = null;
	
	@Override
    public void onCreate() {
		//Set the service to be running
		DbHelper dbHelper = new DbHelper(this);
		db = dbHelper.getWritableDatabase();
		Log.i(SMS_SERVICE, "Database loaded in writable mode...");
		Log.i(SMS_SERVICE, "Database open? " + db.isOpen());
		Log.i(SMS_SERVICE, "Database read only? " + db.isReadOnly());
		
		Cursor results = db.query("SERVICES",
			  new String[]{"SERVICE_NAME", "SERVICE_STARTED"},
			  "SERVICE_NAME = '" + SMS_SERVICE + "'",
			  null, null, null, null
		);
		
		Log.i(SMS_SERVICE, results.getCount() + " results found.");
		while(results.moveToNext()){
			Log.i(SMS_SERVICE, "Result " + (results.getPosition() + 1) + ":");
			Log.i(SMS_SERVICE, "   Service name: " + results.getString(0));
			Log.i(SMS_SERVICE, "   Service started: " + results.getString(1));
		}
		
		if(results.getCount() == 0){
			//Insert new record
			ContentValues values = new ContentValues();
			values.put("SERVICE_NAME", SMS_SERVICE);
			values.put("SERVICE_STARTED", 1);
			db.insert("SERVICES", null, values);
		}else{
			ContentValues values = new ContentValues();
			values.put("SERVICE_STARTED", 1);
			db.update("SERVICES", values, "SERVICE_NAME='" + SMS_SERVICE + "'", null);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.i(SMS_SERVICE, "Service on start command executed");
		
		String connectionType = null;
		Cursor results = db.query("PREFERENCES",
				new String[]{"VALUE"},
				"NAME = 'CONNECTION_TYPE'",
				null, null, null, null
		);
		while(results.moveToNext()){
			connectionType = results.getString(0);
		}
		
		if(connectionType != null){
			switch (Integer.parseInt(connectionType)) {
			case R.id.connectionWifi:
				startWifiListenerThread();
				break;
			case R.id.connectionBT:
				startBTListenerThread();
				break;
			case R.id.connectionUSB:
				startUSBListenerThread();
				break;
			}
		}
		
		return START_NOT_STICKY;
	}
	
	
	private void startWifiListenerThread(){
		Log.d(SMS_SERVICE, "Start Wifi Listener Thread");
		wifiConnection = new WifiConnection();
		connectionThread = new Thread(wifiConnection);
		connectionThread.start();
	}
	
	private void startBTListenerThread(){
		Log.d(SMS_SERVICE, "Start Bluetooth Listener Thread");
	}
	
	private void startUSBListenerThread(){
		Log.d(SMS_SERVICE, "Start USB Listener Thread");
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy(){
		Log.i(SMS_SERVICE, "Service has been destroyed!");
		//Set the service to be stopped
		ContentValues values = new ContentValues();
		values.put("SERVICE_STARTED", 0);
		db.update("SERVICES", values, "SERVICE_NAME='" + SMS_SERVICE + "'", null);
		db.close();
		
		//Close running thread in case the service has been stopped.
		wifiConnection.closeConnection();
	}
}
