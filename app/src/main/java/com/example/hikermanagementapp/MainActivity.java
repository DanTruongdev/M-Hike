package com.example.hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikermanagementapp.adapter.HikeAdapter;
import com.example.hikermanagementapp.db.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btnAddHike;
    TextView tvNoHike , tvNotFound;
    DatabaseHelper db;
    ArrayList<String> hike_id, hike_name, hike_location, hike_dateOfHike, hike_parkingAvailable, hike_length, hike_level, hike_description,
                    hike_estimatedCompletionTime, hike_actualCompletionTime;
    HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hiker Details");
        setContentView(R.layout.activity_main);

        tvNoHike = findViewById(R.id.tvNoHike);
        tvNotFound = findViewById(R.id.tvNotFound);
        recyclerView = findViewById(R.id.hikeRecycleView);
        btnAddHike = findViewById(R.id.btnAddHike);
        btnAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
                startActivity(intent);
            }
        });
        db = new DatabaseHelper(MainActivity.this);
        hike_id = new ArrayList<>();
        hike_name = new ArrayList<>();
        hike_location = new ArrayList<>();
        hike_dateOfHike = new ArrayList<>();
        hike_parkingAvailable = new ArrayList<>();
        hike_length = new ArrayList<>();
        hike_level = new ArrayList<>();
        hike_description = new ArrayList<>();
        hike_estimatedCompletionTime = new ArrayList<>();
        hike_actualCompletionTime = new ArrayList<>();

        getHikeData(1, "GET", null);
        hikeAdapter = new HikeAdapter(MainActivity.this, hike_id, hike_name, hike_location, hike_dateOfHike, hike_parkingAvailable, hike_length, hike_level, hike_description,
                hike_estimatedCompletionTime, hike_actualCompletionTime);
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void getHikeData(int order, String type, String searchString){
        Cursor cursor = type.equals("GET") ? db.getAllHike(order) : db.searchHike(searchString);
        if (cursor.getCount() == 0){
            if (type.equals("GET")) tvNoHike.setVisibility(View.VISIBLE);
            else {
                tvNotFound.setText("No result for \'"+searchString+"\'");
                tvNotFound.setVisibility(View.VISIBLE);
            }
        } else {
            tvNoHike.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                hike_id.add(cursor.getString(0));
                hike_name.add(cursor.getString(1));
                hike_location.add(cursor.getString(2));
                hike_dateOfHike.add(cursor.getString(3));
                hike_parkingAvailable.add(cursor.getString(4));
                hike_length.add(cursor.getString(5));
                hike_level.add(cursor.getString(6));
                hike_description.add(cursor.getString(7));
                hike_estimatedCompletionTime.add(cursor.getString(8));
                hike_actualCompletionTime.add(cursor.getString(9));
            }
        }
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hike_menu, menu);

        MenuItem searchItem =  menu.findItem(R.id.searchHike);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                clearAllAdapterData();
                getHikeData(0, "SEARCH", s);
                hikeAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllHike){
            confirmDialog();
        }
        if (item.getItemId() == R.id.sortHikeAsc){
            sortRecyclerViewItem(0);

        }
        if (item.getItemId() == R.id.sortHikeDecs){
            sortRecyclerViewItem(1);
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortRecyclerViewItem(int order){
        clearAllAdapterData();
        hikeAdapter.notifyDataSetChanged();
        getHikeData(order, "GET", null);
        hikeAdapter.notifyDataSetChanged();
    }

    public  void clearAllAdapterData(){
        hike_id.clear();
        hike_name.clear();
        hike_location.clear();
        hike_dateOfHike.clear();
        hike_parkingAvailable.clear();
        hike_length.clear();
        hike_level.clear();
        hike_description.clear();
        hike_estimatedCompletionTime.clear();
        hike_actualCompletionTime.clear();
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all hike?");
        builder.setMessage("Are you sure to delete all hike ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.deleteAllHikeRow();
                db.close();
                //refresh main activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                Toast.makeText(MainActivity.this, "Removing", Toast.LENGTH_SHORT).show();
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
}