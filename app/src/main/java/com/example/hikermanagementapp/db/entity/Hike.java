package com.example.hikermanagementapp.db.entity;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class Hike {
    public static String TABLE_NAME = "hike";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_LOCATION = "location";
    public static String COLUMN_DATE = "date_of_hike";
    public static String COLUMN_PARKING_AVAILABLE = "parking_available";
    public static String COLUMN_LENGTH = "length";
    public static String COLUMN_LEVEL = "level";
    public static String COLUMN_DESCRIPTION = "description";
    public static String COLUMN_ESTIMATED_COMPLETION_TIME = "estimated_completion_time";
    public static String COLUMN_ACTUAL_COMPLETION_TIME = "actual_completion_time";
    public  static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Hike(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "location TEXT NOT NULL, " +
            "date_of_hike TEXT NOT NULL, " +
            "parking_available BOOLEAN NOT NULL, " +
            "length REAL NOT NULL, " +
            "level TEXT NOT NULL, " +
            "description TEXT, " +
            "estimated_completion_time TEXT, " +
            "actual_completion_time TEXT)";
    private int hikeId;
    private String hikeName;
    private String location;
    private Date dateOfHike;
    private boolean parkingAvailable;
    private float lengthOfHike;
    private String level;
    @Nullable
    private  String description;
    @Nullable
    private  long estimatedCompletionTime; //unit: second
    @Nullable
    private  long actualCompletionTime;

    public Hike(int hikeId, String hikeName, String location, Date dateOfHike, boolean parkingAvailable,
                float lengthOfHike, String level, String description, long estimatedCompletionTime,
                long actualCompletionTime) {

        this.hikeId = hikeId;
        this.hikeName = hikeName;
        this.location = location;
        this.dateOfHike = dateOfHike;
        this.parkingAvailable = parkingAvailable;
        this.lengthOfHike = lengthOfHike;
        this.level = level;
        this.description = description;
        this.estimatedCompletionTime = estimatedCompletionTime;
        this.actualCompletionTime = actualCompletionTime;
    }


    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateOfHike() {
        return dateOfHike;
    }

    public void setDateOfHike(Date dateOfHike) {
        this.dateOfHike = dateOfHike;
    }

    public boolean isParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(boolean parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public float getLengthOfHike() {
        return lengthOfHike;
    }

    public void setLengthOfHike(float lengthOfHike) {
        this.lengthOfHike = lengthOfHike;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public long getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(long estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public long getActualCompletionTime() {
        return actualCompletionTime;
    }

    public void setActualCompletionTime(long actualCompletionTime) {
        this.actualCompletionTime = actualCompletionTime;
    }


}
