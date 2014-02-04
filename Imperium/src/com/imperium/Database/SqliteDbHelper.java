package com.imperium.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DbContract.ActiveSystems.TABLE_NAME + " (" +
            DbContract.ActiveSystems._ID + " INTEGER PRIMARY KEY," +
            DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME + TEXT_TYPE + COMMA_SEP +
            DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS + TEXT_TYPE + COMMA_SEP +
            DbContract.ActiveSystems.COLUMN_NAME_PORT + TEXT_TYPE + COMMA_SEP +
            DbContract.ActiveSystems.COLUMN_STATUS + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + DbContract.ActiveSystems.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Imperium.db";

    public SqliteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openDataBase() throws SQLException {
        mDatabase = getReadableDatabase();
    }

    @Override
    public synchronized void close() {

        if(mDatabase != null)
            mDatabase.close();

        super.close();
    }

    public void insertSystem(System system){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME, system.getName());
        values.put(DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS, system.getIpAddress());
        values.put(DbContract.ActiveSystems.COLUMN_NAME_PORT, system.getPort());
        values.put(DbContract.ActiveSystems.COLUMN_STATUS, system.getStatus());

        mDatabase.insert(
                DbContract.ActiveSystems.TABLE_NAME, null, values);
    }

    public void updateSystem(System system){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME, system.getName());
        values.put(DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS, system.getIpAddress());
        values.put(DbContract.ActiveSystems.COLUMN_NAME_PORT, system.getPort());
        values.put(DbContract.ActiveSystems.COLUMN_STATUS, system.getStatus());

        mDatabase.update(
                DbContract.ActiveSystems.TABLE_NAME, values,
                    String.format("%s='%s'", DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME, system.getName()), null);
    }

    public void deleteSystem(System system){
        String table = DbContract.ActiveSystems.TABLE_NAME;
        mDatabase.delete(table,
                String.format("%s=?",
                    DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME),
                new String[]{system.getName()});
    }

    public System getSystem(String systemName){
        String[] projection = {
                DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME,
                DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS,
                DbContract.ActiveSystems.COLUMN_NAME_PORT,
                DbContract.ActiveSystems.COLUMN_STATUS
        };

        String whereClause = String.format("%s=?",
                DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME);

        String[] whereArgs = new String[]{systemName};

        Cursor c = mDatabase.query(
                DbContract.ActiveSystems.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                whereClause,                              // The columns for the WHERE clause
                whereArgs,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if(c != null && c.getCount() > 0)  {
            c.moveToFirst();

            System system = new System();
            system.setName(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME)
            ));
            system.setIpAddress(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS)
            ));
            system.setPort(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_PORT)
            ));
            system.setStatus(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_STATUS)
            ));

            return system;
        }
        else return null;
    }

    public List<System> getAllSystems(){
        String[] projection = {
            DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME,
            DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS,
            DbContract.ActiveSystems.COLUMN_NAME_PORT,
            DbContract.ActiveSystems.COLUMN_STATUS
        };

        Cursor c = mDatabase.query(
                DbContract.ActiveSystems.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );
        c.moveToFirst();

        ArrayList<System> systems = new ArrayList<System>();
        while (c.isAfterLast() == false)
        {
            System system = new System();
            system.setName(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_SYSTEM_NAME)
            ));
            system.setIpAddress(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_IP_ADDRESS)
            ));
            system.setPort(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_NAME_PORT)
            ));
            system.setStatus(c.getString(c.getColumnIndexOrThrow(
                    DbContract.ActiveSystems.COLUMN_STATUS)
            ));

            c.moveToNext();
            systems.add(system);
        }

        return systems;
    }

    public void deleteAllSystems(){
        String table = DbContract.ActiveSystems.TABLE_NAME;
        mDatabase.delete(table, String.format("_id IN (SELECT _id FROM %s)", table), null);
    }
}
