package com.example.hikermanagementapp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hikermanagementapp.db.DatabaseHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddObservationActivity extends AppCompatActivity {
    private final static int REQUEST_CODE = 100;
    private static final Calendar calendar= Calendar.getInstance();
    private SharedPreferences prefs;
    String hikeId;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText edtObservationName, edtObservationLocation, edtObservationDateTime, edtObservationDescription;
    ImageView imgObservation;
    Button btnSaveObservation;
    FloatingActionButton btnCurrentLocation;
    Uri uri;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        hikeId = prefs.getString("hikeId", null);
        edtObservationName = findViewById(R.id.edtObservationName);
        edtObservationLocation = findViewById(R.id.edtObservationLocation);
        edtObservationDateTime = findViewById(R.id.edtObservationDateTime);
        edtObservationDateTime.setText(dateTimeFormatter(calendar.getTime()));
        edtObservationDescription = findViewById(R.id.edtObservationDescription);
        imgObservation = findViewById(R.id.imgObservation);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        edtObservationDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(edtObservationDateTime);
            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLocation();
            }
        });


        imgObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission
                handleUploadImages();
            }
        });

        btnSaveObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                ArrayList<EditText> edtList = new ArrayList<EditText>(Arrays.asList(edtObservationName, edtObservationLocation,  edtObservationDescription));
                if (edtObservationName.getText().toString().length() == 0){
                    edtObservationName.setError("This field is required");
                } else {
                    // Handle image
                    imgObservation.setDrawingCacheEnabled(true);
                    imgObservation.buildDrawingCache();
                    Bitmap bitmap = imgObservation.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    //others data
                    String location = edtObservationLocation.getText().toString().trim().length() == 0 ? "" : edtObservationLocation.getText().toString().trim();
                    String description = edtObservationDescription.getText().toString().trim().length() == 0 ? "" : edtObservationDescription.getText().toString().trim();

                    //add data to sqlite
                    db = new DatabaseHelper(AddObservationActivity.this);
                    db.addObservation(Integer.valueOf(hikeId), edtObservationName.getText().toString().trim(), location, edtObservationDateTime.getText().toString().trim(), byteArray, description);
                    imgObservation.setImageResource(R.drawable.ic_camera);
                    setAllFieldEmpty(edtList);
                }
            }
        });

    }


    private String dateTimeFormatter(Date date){
        String customFormat="yy/MM/dd HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(customFormat, Locale.UK);
        return dateFormat.format(date);
    }
    private void showDateTimeDialog(final EditText editText) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        editText.setText(dateTimeFormatter(calendar.getTime()));
                    }
                };
                new TimePickerDialog(AddObservationActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(AddObservationActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleUploadImages() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ImagePicker.with(AddObservationActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(AddObservationActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            imgObservation.setImageURI(uri);
        } else {
            Toast.makeText(AddObservationActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null){

                                convertToAddress(location.getLatitude()+"", location.getLongitude()+"");
                            } else{
                                Toast.makeText(AddObservationActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(AddObservationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
    private void convertToAddress(String latitude, String longitude){

        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String address = response.getString("display_name");
                    edtObservationLocation.setText(address);
                } catch (JSONException e) {
                    Toast.makeText(AddObservationActivity.this, "not ok", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                edtObservationLocation.setText("Error");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                handleLocation();
            }
        } else {
            Toast.makeText(this, "Required permission", Toast.LENGTH_SHORT).show();
        }


    }

    private void setAllFieldEmpty(ArrayList<EditText> edtList){
        for (EditText edt:edtList) {
            edt.setText("");
        }
    }
}


