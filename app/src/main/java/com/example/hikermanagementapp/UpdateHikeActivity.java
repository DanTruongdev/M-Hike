package com.example.hikermanagementapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hikermanagementapp.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class UpdateHikeActivity extends AppCompatActivity {
    private static final Calendar calendar= Calendar.getInstance();
    private static final String[] dropdownItems = {"True", "False"};
    private EditText edtHikeName, edtLocation,edtDateOfHike,  edtLength, edtEstimatedHrs, edtEstimatedMins, editEstimatedSecs,
            edtActualHrs,edtActualMins,edtActualSecs , edtDescription;
    String id, name, location, dateOfHike, parkingAvailable, length, level, description, estimatedCompletionTime, actualCompletionTime;
    private Spinner spnParkingAvailable;
    private RadioGroup rdgLevel;
    private Button btnUpdate, btnObservation, btnDelete;
    private RadioButton easyButton;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);
        edtHikeName = findViewById(R.id.edtHikeName2);
        edtLocation = findViewById(R.id.edtLocation2);
        spnParkingAvailable = findViewById(R.id.spnParkingAvailable2);
        edtDateOfHike = findViewById(R.id.edtDateOfHike2);
        edtLength = findViewById(R.id.edtLengthHike2);
        edtEstimatedHrs = findViewById(R.id.edtHours2);
        edtEstimatedMins = findViewById(R.id.edtMinutes2);
        editEstimatedSecs = findViewById(R.id.edtSeconds2);
        edtActualHrs = findViewById(R.id.edtActualHours);
        edtActualMins = findViewById(R.id.edtActualMinutes);
        edtActualSecs = findViewById(R.id.edtActualSeconds);
        spnParkingAvailable = findViewById(R.id.spnParkingAvailable2);
        rdgLevel = findViewById(R.id.rdgLevel2);
        easyButton = findViewById((R.id.rdEasy2));
        edtDescription= findViewById(R.id.edtDescription2);
        btnUpdate = findViewById(R.id.btnUpdateHike);
        btnObservation = findViewById(R.id.btnViewObservation);
        btnDelete = findViewById(R.id.btnDeleteHike);
        //for date of hike
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        edtDateOfHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateHikeActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateHikeActivity.this, android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnParkingAvailable.setAdapter(adapter);

        //set data

        getAndSetIntentData();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(name);
        }

        //for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                ArrayList<EditText> edtList = new ArrayList<EditText>(Arrays.asList(edtHikeName, edtLocation,edtDateOfHike,  edtLength));
                if(checkAllField(edtList)){
                    //get value
                    db = new DatabaseHelper(UpdateHikeActivity.this);
                    String selectedValue = spnParkingAvailable.getSelectedItem().toString();
                    boolean parkingAvailable = selectedValue.equals("True") ? true : false;
                    RadioButton checkedRadioButton = findViewById(rdgLevel.getCheckedRadioButtonId());
                    int estimatedHrs =  edtEstimatedHrs.getText().toString().length() != 0 ? Integer.parseInt(edtEstimatedHrs.getText().toString().trim()) : 0;
                    int estimatedMins = edtEstimatedMins.getText().toString().length() != 0 ? Integer.parseInt(edtEstimatedMins.getText().toString().trim()) : 0;
                    int estimatedSecs = editEstimatedSecs.getText().toString().length() != 0 ? Integer.parseInt(editEstimatedSecs.getText().toString().trim()) : 0;
                    int actualHrs = edtActualHrs.getText().toString().length() != 0 ? Integer.parseInt(edtActualHrs.getText().toString().trim()) : 0;
                    int actualMins =  edtActualMins.getText().toString().length() != 0 ? Integer.parseInt(edtActualMins.getText().toString().trim()) : 0;
                    int actualSecs =  edtActualSecs.getText().toString().length() != 0 ? Integer.parseInt(edtActualSecs.getText().toString().trim()) : 0;
                    long estimatedTime = (estimatedHrs*3600) + (estimatedMins*60) + estimatedSecs;
                    long actualTime = (actualHrs*3600) + (actualMins*60) + actualSecs;

                    //update hike
                    db.updateHike(id+"", edtHikeName.getText().toString().trim(), edtLocation.getText().toString().trim(), edtDateOfHike.getText().toString().trim(), parkingAvailable,
                            Float.valueOf(edtLength.getText().toString().trim()), checkedRadioButton.getText().toString().trim(), edtDescription.getText().toString().trim(), estimatedTime, actualTime);
                }
            }
        });

        btnObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateHikeActivity.this, ObservationActivity.class);
                intent.putExtra("hike_id", id);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }
        //get intent data
        private void getAndSetIntentData(){
            if (getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("location") && getIntent().hasExtra("dateOfHike")
                    && getIntent().hasExtra("parkingAvailable") && getIntent().hasExtra("length") && getIntent().hasExtra("level") && getIntent().hasExtra("description")
                    && getIntent().hasExtra("estimatedCompletionTime") && getIntent().hasExtra("actualCompletionTime")){
                //get data
                id = getIntent().getStringExtra("id");
                name = getIntent().getStringExtra("name");
                location = getIntent().getStringExtra("location");
                dateOfHike = getIntent().getStringExtra("dateOfHike");
                parkingAvailable = getIntent().getStringExtra("parkingAvailable");
                length = getIntent().getStringExtra("length");
                level = getIntent().getStringExtra("level");
                description = getIntent().getStringExtra("description");
                estimatedCompletionTime = getIntent().getStringExtra("estimatedCompletionTime");
                actualCompletionTime = getIntent().getStringExtra("actualCompletionTime");
                //set data
                edtHikeName.setText(name);
                edtLocation.setText(location);
                edtDateOfHike.setText(dateOfHike);
                if (parkingAvailable.equals("True")) spnParkingAvailable.setSelection(0);
                else spnParkingAvailable.setSelection(1);
                edtLength.setText(length);
                Long estimateTime = Long.valueOf(estimatedCompletionTime).longValue();
                edtEstimatedHrs.setText(estimateTime/3600+"");
                estimateTime = estimateTime - 3600 * (estimateTime / 3600);
                edtEstimatedMins.setText(estimateTime / 60+"");
                estimateTime = estimateTime - 60 * (estimateTime / 60);
                editEstimatedSecs.setText(estimateTime+"");
                Long actualTime = Long.valueOf(actualCompletionTime).longValue();
                edtActualHrs.setText(actualTime / 3600+"");
                actualTime = actualTime - 3600 * (actualTime / 3600);
                edtActualMins.setText(actualTime / 60+"");
                actualTime = actualTime - 60 * (actualTime / 60);
                edtActualSecs.setText(actualTime+"");
                RadioButton checkedButton;
                switch (level){
                    case "Easy":
                        checkedButton = findViewById(R.id.rdEasy2);
                        checkedButton.setChecked(true);
                        break;
                    case "Medium":
                        checkedButton = findViewById(R.id.rdMedium2);
                        checkedButton.setChecked(true);
                        break;
                    case "Hard":
                        checkedButton = findViewById(R.id.rdHard2);
                        checkedButton.setChecked(true);
                        break;
                    case "Insane":
                        checkedButton = findViewById(R.id.rdInsane2);
                        checkedButton.setChecked(true);
                        break;
                }
                edtDescription.setText(description);
            }  else {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }
        }
        private void updateLabel(){
            String myFormat="MM/dd/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
            edtDateOfHike.setText(dateFormat.format(calendar.getTime()));
        }
        private boolean checkAllField(ArrayList<EditText> edtList ){
            boolean result = true;
            for (EditText edt:edtList) {
                if (edt.getText().toString().trim().length() == 0){
                    edt.setError("This field is required");
                    result = false;
                }
            }
            return  result;
        }
        private void confirmDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete "+name+"?");
            builder.setMessage("Are you sure to delete "+name+"?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseHelper db = new DatabaseHelper(UpdateHikeActivity.this);
                    db.deleteOneHikeRow(id);
                    Intent intent = new Intent(UpdateHikeActivity.this, MainActivity.class);
                    Toast.makeText(UpdateHikeActivity.this, "Delete hike successfully", Toast.LENGTH_SHORT).show();
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