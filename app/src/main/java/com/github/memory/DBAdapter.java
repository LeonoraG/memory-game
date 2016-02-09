package com.github.memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//id je kljuc, ime, vrijeme ( u sekundama), broj pokusaja, broj kartica
public  class DBAdapter  {
    static final String KEY_ROWID  = "_id";
    static final String KEY_NAME   = "name";
    static final String KEY_TIME   = "time";
    static final String KEY_TRIES  = "tries";
    static final String KEY_CARDNO = "cardno";

    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME  = "MyDB";
    static final String DATABASE_TABLE = "scores";
    static final int DATABASE_VERSION  = 2;

    static final String DATABASE_CREATE =
            "create table scores (_id integer primary key autoincrement, "
                    + "name text not null, "
                    + "time integer not null, "
                    + "tries integer not null, "
                    + "cardno integer not null "
                    + ");";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS scores");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a new score into the database---
    public long insertScore(String name, int time, int tries, int cardno)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_TRIES, tries);
        initialValues.put(KEY_CARDNO, cardno);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---retrieves all the scores---
    public Cursor getAllScores()
    {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME,
                KEY_TIME, KEY_TRIES, KEY_CARDNO}, null, null, null, null, null);
    }

    //---get all scores with given number of cards, sorted by time elapsed---
    public Cursor getSortedByTimeElapsed(int cardNumber) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_TIME, KEY_TRIES, KEY_CARDNO}, KEY_CARDNO + "=" + cardNumber, null,
                        null, null, KEY_TIME+" ASC", null);

        return mCursor;
    }

    //---get all scores with given number of cards, sorted by number of tries elapsed---
    public Cursor getSortedByNumberOfTries(int cardNumber) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAME, KEY_TIME, KEY_TRIES, KEY_CARDNO}, KEY_CARDNO + "=" + cardNumber, null,
                        null, null, KEY_TRIES+" ASC", null);

        return mCursor;
    }

    public void reset () throws SQLException {
        db = DBHelper.getWritableDatabase ();
        db.execSQL("delete * from scores");
        db.execSQL(DATABASE_CREATE);
    }



}
