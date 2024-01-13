package com.example.hikermanagementapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.UpdateObservationActivity;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> observation_id, observation_name, observation_location, observation_timeOfObservation,  observation_description;
    ArrayList<Bitmap> observation_image;
    private int position;

    public ObservationAdapter(Context context, ArrayList<String> observation_id, ArrayList<String> observation_name, ArrayList<String> observation_location, ArrayList<String> observation_timeOfObservation, ArrayList<String> observation_description, ArrayList<Bitmap> observation_image) {
        this.context = context;
        this.observation_id = observation_id;
        this.observation_name = observation_name;
        this.observation_location = observation_location;
        this.observation_timeOfObservation = observation_timeOfObservation;
        this.observation_description = observation_description;
        this.observation_image = observation_image;
    }

    @NonNull
    @Override
    public ObservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.observation_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.MyViewHolder holder, int position) {
        Log.i("Binding","new binding");
        Log.i("Id = ",observation_id.get(position)+"");

        holder.txtObservationId.setText(String.valueOf(observation_id.get(position)));
        holder.txtObservationName.setText(String.valueOf(observation_name.get(position)));
        holder.txtObservationLocation.setText(String.valueOf(observation_location.get(position)));
        holder.imageObservation.setImageBitmap(observation_image.get(position));
        holder.txtTimeOfObservation.setText(String.valueOf(observation_timeOfObservation.get(position)));

        //handle onclick event
        this.position = position;
        holder.mainObservationItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                observation_image.get(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                Intent intent = new Intent(context, UpdateObservationActivity.class);

                intent.putExtra("observation_id", observation_id.get(position)+"");
                intent.putExtra("observation_name", observation_name.get(position)+"");
                intent.putExtra("observation_location", observation_location.get(position)+"");
                intent.putExtra("observation_timeOfObservation", observation_timeOfObservation.get(position)+"");
                intent.putExtra("observation_image", imageBytes);
                intent.putExtra("observation_description", observation_description.get(position)+"");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observation_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtObservationId, txtObservationName, txtObservationLocation, txtTimeOfObservation;
        ImageView imageObservation;
        LinearLayout mainObservationItemLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtObservationId = itemView.findViewById(R.id.txtObservationId);
            txtObservationName = itemView.findViewById(R.id.txtObservationName);
            txtObservationLocation = itemView.findViewById(R.id.txtObservationLocation);
            txtTimeOfObservation = itemView.findViewById(R.id.txtObservationTime);
            imageObservation = itemView.findViewById(R.id.imageObservation);
            mainObservationItemLayout = itemView.findViewById(R.id.mainObservationItemLayout);
        }
    }
}
