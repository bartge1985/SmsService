package ext.atos.mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SMSSERVICE.db";
	private static final int DATABASE_VERSION = 2;
    private static final String SERVICES_TABLE_NAME = "SERVICES";
    private static final String PREFERENCES_TABLE_NAME = "PREFERENCES";
    private static final String SERVICE_NAME = "SERVICE_NAME";
    private static final String SERVICE_STARTED = "SERVICE_STARTED";
    private static final String PREFERENCE_NAME = "NAME";
    private static final String PREFERENCE_VALUE = "VALUE";
    private static final String SERVICES_TABLE_CREATE =
                "CREATE TABLE " + SERVICES_TABLE_NAME + " (" +
                SERVICE_NAME + " TEXT, " +
                SERVICE_STARTED + " NUMBER);";
    private static final String PREFERENCES_TABLE_CREATE =
        "CREATE TABLE " + PREFERENCES_TABLE_NAME + " (" +
        PREFERENCE_NAME + " TEXT, " +
        PREFERENCE_VALUE + " TEXT);";
    private static final String PREFERENCES_INSERT_PREF =
    			"INSERT INTO " + PREFERENCES_TABLE_NAME +
    			"(" + PREFERENCE_NAME + ")" +
    			"VALUES ('CONNECTION_TYPE');"; 

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SERVICES_TABLE_CREATE);
        db.execSQL(PREFERENCES_TABLE_CREATE);
        db.execSQL(PREFERENCES_INSERT_PREF);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//do nothing in case of update
	}
}
