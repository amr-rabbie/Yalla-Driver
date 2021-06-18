/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package LoingSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import static java.sql.Types.REF;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "YallaDriver";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String SHIFT_ID = "shift_id";
    private static final String COMPANY_ID = "company_id";
    private static final String TAXI_NO = "taxi_no";
    private static final String REF_CODE = "ref_code";
    private static final String CAR_NAME = "car_name";
    private static final String PRO_PIC="pro_pic";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE "
                + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + SHIFT_ID + " TEXT,"
                + COMPANY_ID + " TEXT,"
                + TAXI_NO + " TEXT,"
                + REF_CODE + " TEXT,"
                + CAR_NAME + " TEXT,"
                + PRO_PIC + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String shift_id, String company_id, String taxi_no, String ref_code, String car_name,String pro_pic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UID, uid);
        values.put(SHIFT_ID, shift_id);
        values.put(COMPANY_ID, company_id);
        values.put(TAXI_NO, taxi_no);
        values.put(REF_CODE, ref_code);
        values.put(CAR_NAME, car_name);
        values.put(PRO_PIC,pro_pic);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection


    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("shift_id", cursor.getString(4));
            user.put("company_id", cursor.getString(5));
            user.put("taxi_no", cursor.getString(6));
            user.put("ref_code", cursor.getString(7));
            user.put("car_name", cursor.getString(8));
            user.put("pro_pic", cursor.getString(9));

        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();


    }

}
