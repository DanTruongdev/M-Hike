package com.example.hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikermanagementapp.adapter.HikeAdapter;
import com.example.hikermanagementapp.adapter.ObservationAdapter;
import com.example.hikermanagementapp.db.DatabaseHelper;
import com.example.hikermanagementapp.db.entity.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ObservationActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    RecyclerView observationRecyclerView;
    String hikeId;
    FloatingActionButton btnAddObservation;
    TextView tvNoObservation;
    DatabaseHelper db;
    ObservationAdapter observationAdapter;
    ArrayList<String> observation_id, observation_name, observation_location, observation_timeOfObservation,  observation_description;
    ArrayList<Bitmap> observation_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        observationRecyclerView = findViewById(R.id.observationRecycleView);
        btnAddObservation = findViewById(R.id.btnAddObservation);
        tvNoObservation = findViewById(R.id.tvNoObservation);

        if(getIntent().hasExtra("hike_id")) {
            String value = prefs.getString("hikeId", null);
            //check if pref contains "hike id" key
            if (value != null){
                prefs.edit().remove("hikeId");
                prefs.edit().apply();
            }
            prefs.edit().putString("hikeId", getIntent().getStringExtra("hike_id")).commit();
            prefs.edit().apply();
        }
        hikeId = prefs.getString("hikeId", null);

        db = new DatabaseHelper(this);
        observation_id = new ArrayList<>();
        observation_name = new ArrayList<>();
        observation_location = new ArrayList<>();
        observation_timeOfObservation = new ArrayList<>();
        observation_image = new ArrayList<>();
        observation_description = new ArrayList<>();

        getObservationData(1);
        observationAdapter = new ObservationAdapter(ObservationActivity.this, observation_id, observation_name, observation_location, observation_timeOfObservation,  observation_description, observation_image);
        observationRecyclerView.setAdapter(observationAdapter);
        observationRecyclerView.setLayoutManager(new LinearLayoutManager(ObservationActivity.this));
        btnAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObservationActivity.this, AddObservationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.observation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllObservation){
            confirmDialog();
        }
        if (item.getItemId() == R.id.sortObservationAsc){
            sortRecyclerViewItem(0);
        }
        if (item.getItemId() == R.id.sortObservationDecs){
            sortRecyclerViewItem(1);
        }
        return super.onOptionsItemSelected(item);
    }
    public void sortRecyclerViewItem(int order){
        observation_id.clear();
        observation_name.clear();
        observation_location.clear();
        observation_timeOfObservation.clear();
        observation_image.clear();
        observation_description.clear();
        observationAdapter.notifyDataSetChanged();
        getObservationData(order);
        observationAdapter.notifyDataSetChanged();
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all observation?");
        builder.setMessage("Are you sure to delete all observation ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(ObservationActivity.this);
                db.deleteAllObservationRow();
                db.close();
                //refresh main activity
                Intent intent = new Intent(ObservationActivity.this, ObservationActivity.class);
                Toast.makeText(ObservationActivity.this, "Removing", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @SuppressLint("Range")
    public void getObservationData(int order){
        Cursor cursor = db.getObservationByHikeId(hikeId, order);

        if (cursor.getCount() == 0){
            tvNoObservation.setVisibility(View.VISIBLE);
        } else {
            tvNoObservation.setVisibility(View.GONE);
            byte[] imageBytes;
            while (cursor.moveToNext()){

                observation_id.add(cursor.getString(cursor.getColumnIndex(Observation.COLUMN_ID)));
                observation_name.add(cursor.getString(cursor.getColumnIndex(Observation.COLUMN_NAME)));
                observation_location.add(cursor.getString(cursor.getColumnIndex(Observation.COLUMN_LOCATION)));
                observation_timeOfObservation.add(cursor.getString(cursor.getColumnIndex(Observation.COLUMN_TIME)));
                imageBytes = cursor.getBlob(cursor.getColumnIndex(Observation.COLUMN_IMAGE));
                observation_image.add(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                observation_description.add(cursor.getString(cursor.getColumnIndex(Observation.COLUMN_DESCRIPTION)));
            }
        }
        cursor.close();
    }


}
