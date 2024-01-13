package com.example.hikermanagementapp.db.entity;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.Date;

public class Observation {
    public static String TABLE_NAME = "observation";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_HIKE_ID = "hike_id";
    public static String COLUMN_NAME = "observation_name";
    public static String COLUMN_LOCATION = "location";
    public static String COLUMN_TIME = "time_of_observation";
    public static String COLUMN_IMAGE = "image";
    public static String COLUMN_DESCRIPTION = "description";


    public  static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Observation(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "hike_id INTEGER NOT NULL, " +
            "observation_name TEXT NOT NULL, " +
            "location TEXT NOT NULL, " +
            "time_of_observation BLOB, " +
            "image BLOB NOT NULL, " +
            "description TEXT)";
    private int observationId;
    private int hikeId;
    private String observationName;
    @Nullable
    private String location;
    private Date timeOfObservation;
    @Nullable
    private Bitmap image;
    @Nullable
    private  String description;

    public Observation(int observationId, int hikeId, String observationName, @Nullable String location, Date timeOfObservation, @Nullable String description) {
        this.observationId = observationId;
        this.hikeId = hikeId;
        this.observationName = observationName;
        this.location = location;
        this.timeOfObservation = timeOfObservation;
        this.description = description;
    }

    public int getObservationId() {
        return observationId;
    }

    public void setObservationId(int observationId) {
        this.observationId = observationId;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getObservationName() {
        return observationName;
    }

    public void setObservationName(String observationName) {
        this.observationName = observationName;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    public Date getTimeOfObservation() {
        return timeOfObservation;
    }

    public void setTimeOfObservation(Date timeOfObservation) {
        this.timeOfObservation = timeOfObservation;
    }

    @Nullable
    public Bitmap getImage() {
        return image;
    }

    public void setImage(@Nullable Bitmap image) {
        this.image = image;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
