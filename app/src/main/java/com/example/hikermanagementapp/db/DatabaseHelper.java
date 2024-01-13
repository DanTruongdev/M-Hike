package com.example.hikermanagementapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.hikermanagementapp.db.entity.Hike;
import com.example.hikermanagementapp.db.entity.Observation;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "HikerManagement.db";
    public static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
        this.context = context;
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Hike.CREATE_TABLE);
        db.execSQL(Observation.CREATE_TABLE);
        this.context = context;
        Log.i("Create", "Create database successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Hike.TABLE_NAME) ;
        db.execSQL("DROP TABLE IF EXISTS " + Observation.TABLE_NAME) ;
        onCreate(db);
    }

    //HIKE
    public Cursor getAllHike(int order){
        String query = "SELECT * FROM " + Hike.TABLE_NAME+ " ORDER BY "+Hike.COLUMN_DATE;
        if (order == 1) query += " DESC";
        else if (order == 0) query += " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public void addHike(String hikeName, String location, String dateOfHike, boolean parkingAvailable,
                        float lengthOfHike, String level, String description, long estimatedCompletionTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike.COLUMN_NAME, hikeName);
        values.put(Hike.COLUMN_LOCATION, location);
        values.put(Hike.COLUMN_DATE, dateOfHike);
        values.put(Hike.COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(Hike.COLUMN_LENGTH, lengthOfHike);
        values.put(Hike.COLUMN_LEVEL, level);
        values.put(Hike.COLUMN_DESCRIPTION, description);
        values.put(Hike.COLUMN_ESTIMATED_COMPLETION_TIME, ""+estimatedCompletionTime);
        values.put(Hike.COLUMN_ACTUAL_COMPLETION_TIME, "0");
        long result = db.insert(Hike.TABLE_NAME, null, values);
        db.close();
        if (result == -1){
            Toast.makeText(context, "Failed to add hike", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Add hike successfully", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateHike(String hikeId, String hikeName, String location, String dateOfHike, boolean parkingAvailable,
                           float lengthOfHike, String level, String description, long estimatedCompletionTime, long actualCompletionTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike.COLUMN_NAME, hikeName);
        values.put(Hike.COLUMN_LOCATION, location);
        values.put(Hike.COLUMN_DATE, dateOfHike);
        values.put(Hike.COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(Hike.COLUMN_LENGTH, lengthOfHike);
        values.put(Hike.COLUMN_LEVEL, level);
        values.put(Hike.COLUMN_DESCRIPTION, description);
        values.put(Hike.COLUMN_ESTIMATED_COMPLETION_TIME, ""+estimatedCompletionTime);
        values.put(Hike.COLUMN_ACTUAL_COMPLETION_TIME, ""+actualCompletionTime);
        long result = db.update(Hike.TABLE_NAME, values, Hike.COLUMN_ID+"=?", new String[]{hikeId});
        db.close();
        if (result == -1){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Update hike successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneHikeRow(String rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Hike.TABLE_NAME, Hike.COLUMN_ID+"=?", new String[]{rowId});
        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Delete hike successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public  void deleteAllHikeRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Hike.TABLE_NAME);
    }

    public Cursor searchHike(String searchString){
        String query = "SELECT * FROM " + Hike.TABLE_NAME+ " WHERE " + Hike.COLUMN_NAME + " LIKE '%" + searchString + "%'"
                + " OR " + Hike.COLUMN_LOCATION + " LIKE '%" + searchString + "%'"
                + " OR " + Hike.COLUMN_LENGTH + " LIKE '%" + searchString + "%'"
                + " OR " + Hike.COLUMN_DATE + " LIKE '%" + searchString + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //OBSERVATION

    public Cursor getObservationByHikeId(String hikeId, int order){

        String query = "SELECT * FROM " + Observation.TABLE_NAME+ " WHERE " + Observation.COLUMN_HIKE_ID + " = " + hikeId + " ORDER BY "+Observation.COLUMN_TIME;
        if (order == 1) query += " DESC";
        else if (order == 0) query += " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addObservation(int hikeId, String observationName, String location, String timeOfObsevation, byte[] image, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Observation.COLUMN_HIKE_ID, hikeId);
        values.put(Observation.COLUMN_NAME, observationName);
        values.put(Observation.COLUMN_LOCATION, location);
        values.put(Observation.COLUMN_TIME, timeOfObsevation);
        values.put(Observation.COLUMN_IMAGE, image);
        values.put(Hike.COLUMN_DESCRIPTION, description);

        long result = db.insert(Observation.TABLE_NAME, null, values);
        db.close();
        if (result == -1){
            Toast.makeText(context, "Failed to add observation", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Add observation successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateObservation(int observationId ,int hikeId, String observationName, String location, String timeOfObsevation, byte[] image, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Observation.COLUMN_HIKE_ID, hikeId);
        values.put(Observation.COLUMN_NAME, observationName);
        values.put(Observation.COLUMN_LOCATION, location);
        values.put(Observation.COLUMN_TIME, timeOfObsevation);
        values.put(Observation.COLUMN_IMAGE, image);
        values.put(Hike.COLUMN_DESCRIPTION, description);
        long result = db.update(Observation.TABLE_NAME, values, Observation.COLUMN_ID+"=?", new String[]{observationId+""});
        db.close();

        if (result == -1){
            Toast.makeText(context, "Failed to update observation", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Update observation successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneObservationRow(String rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Observation.TABLE_NAME, Observation.COLUMN_ID+"=?", new String[]{rowId});
        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Delete observation successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public  void deleteAllObservationRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Observation.TABLE_NAME);
    }

}
