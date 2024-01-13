package com.example.hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import com.example.hikermanagementapp.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {
    private static final Calendar calendar= Calendar.getInstance();
    private static final String[] dropdownItems = {"True", "False"};
    private EditText edtHikeName, edtLocation,edtDateOfHike,  edtLength, edtHrs, edtMins, editSecs, edtDescription;
    private Spinner spnParkingAvailable;
    private RadioGroup rdgLevel;
    private Button btnSave;
    private RadioButton easyButton;
    private DatabaseHelper db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        edtHikeName = findViewById(R.id.edtHikeName);
        edtLocation = findViewById(R.id.edtHikeLocation);
        spnParkingAvailable = findViewById(R.id.spnParkingAvailable);
        edtDateOfHike = findViewById(R.id.edtDateOfHike);
        edtLength = findViewById(R.id.edtLengthHike);
        edtHrs = findViewById(R.id.edtHours);
        edtMins = findViewById(R.id.edtMinutes);
        editSecs = findViewById(R.id.edtSeconds);
        spnParkingAvailable = findViewById(R.id.spnParkingAvailable);
        rdgLevel = findViewById(R.id.rdgLevel);
        easyButton = findViewById((R.id.rdEasy));
        edtDescription= findViewById(R.id.edtHikeDescription);
        btnSave = findViewById(R.id.btnSaveHike);

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
                new DatePickerDialog(AddHikeActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddHikeActivity.this, android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnParkingAvailable.setAdapter(adapter);

        //for radio group
        easyButton.setChecked(true);

        //for save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                 ArrayList<EditText> edtList = new ArrayList<EditText>(Arrays.asList(edtHikeName, edtLocation,edtDateOfHike,  edtLength));

                 if(checkAllField(edtList)){
                     //get value
                     db = new DatabaseHelper(AddHikeActivity.this);
                     String selectedValue = spnParkingAvailable.getSelectedItem().toString();
                     boolean parkingAvailable = selectedValue.equals("True") ? true : false;
                     RadioButton checkedRadioButton = findViewById(rdgLevel.getCheckedRadioButtonId());
                     int hrs =  edtHrs.getText().toString().length() != 0 ? Integer.parseInt(edtHrs.getText().toString().trim()) : 0;
                     int mins = edtMins.getText().toString().length() != 0 ? Integer.parseInt(edtMins.getText().toString().trim()) : 0;
                     int secs = editSecs.getText().toString().length() != 0 ? Integer.parseInt(editSecs.getText().toString().trim()) : 0;
                     long estimatedTime = (hrs*3600) + (mins*60) + secs;

                     //add to slqlite
                     db.addHike(edtHikeName.getText().toString().trim(), edtLocation.getText().toString().trim(), edtDateOfHike.getText().toString().trim(), parkingAvailable,
                             Float.valueOf(edtLength.getText().toString().trim()), checkedRadioButton.getText().toString().trim(), edtDescription.getText().toString().trim(), estimatedTime);
                     Log.i("Click", "onClick: Add hike successfully");
                     edtList.add(edtHrs);
                     edtList.add(edtMins);
                     edtList.add(editSecs);
                     edtList.add(edtDescription);
                     setAllFieldEmpty(edtList);
                 }


            }
        });

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

    private void setAllFieldEmpty(ArrayList<EditText> edtList){
        for (EditText edt:edtList) {
            edt.setText("");
        }
    }

}