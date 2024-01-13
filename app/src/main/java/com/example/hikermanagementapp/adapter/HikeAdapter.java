package com.example.hikermanagementapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.UpdateHikeActivity;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> {

    Context context;
    private ArrayList<String> hike_id, hike_name, hike_location, hike_dateOfHike, hike_parkingAvailable, hike_length, hike_level, hike_description,
            hike_estimatedCompletionTime, hike_actualCompletionTime;
    private int position;

    public HikeAdapter(Context context, ArrayList<String> hike_id, ArrayList<String> hike_name, ArrayList<String> hike_location, ArrayList<String> hike_dateOfHike,
                       ArrayList<String> hike_parkingAvailable, ArrayList<String> hike_length, ArrayList<String> hike_level, ArrayList<String> hike_description,
                       ArrayList<String> hike_estimatedCompletionTime, ArrayList<String> hike_actualCompletionTime) {
        this.context = context;
        this.hike_id = hike_id;
        this.hike_name = hike_name;
        this.hike_location = hike_location;
        this.hike_dateOfHike = hike_dateOfHike;
        this.hike_parkingAvailable = hike_parkingAvailable;
        this.hike_length = hike_length;
        this.hike_level = hike_level;
        this.hike_description = hike_description;
        this.hike_estimatedCompletionTime = hike_estimatedCompletionTime;
        this.hike_actualCompletionTime = hike_actualCompletionTime;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hike_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvHikeId.setText(String.valueOf(hike_id.get(position)));
        holder.tvHikeName.setText(String.valueOf(hike_name.get(position)));
        holder.tvHikeLocation.setText(String.valueOf(hike_location.get(position)));
        switch (String.valueOf(hike_level.get(position))){
            case "Easy":
                holder.tvHikeLevel.setBackgroundResource(R.color.easy);
                break;
            case "Medium":
                holder.tvHikeLevel.setBackgroundResource(R.color.medium);
                break;
            case "Hard":
                holder.tvHikeLevel.setBackgroundResource(R.color.hard);
                break;
            case "Insane":
                holder.tvHikeLevel.setBackgroundResource(R.color.insane);
                break;
        }
        holder.tvHikeLevel.setText(String.valueOf(hike_level.get(position)));
        holder.tvHikeLength.setText(String.valueOf(hike_length.get(position))+"Km");
        holder.tvHikeDate.setText(String.valueOf(hike_dateOfHike.get(position)));

        //handle onclick event
        this.position = position;
        holder.mainHikeItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateHikeActivity.class);
                intent.putExtra("id", hike_id.get(position)+"");
                intent.putExtra("name", hike_name.get(position)+"");
                intent.putExtra("location", hike_location.get(position)+"");
                intent.putExtra("dateOfHike", hike_dateOfHike.get(position)+"");
                intent.putExtra("parkingAvailable", hike_parkingAvailable.get(position)+"");
                intent.putExtra("length", hike_length.get(position)+"");
                intent.putExtra("level", hike_level.get(position)+"");
                intent.putExtra("description", hike_description.get(position)+"");
                intent.putExtra("estimatedCompletionTime", hike_estimatedCompletionTime.get(position)+"");
                intent.putExtra("actualCompletionTime", hike_actualCompletionTime.get(position)+"");
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return hike_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHikeId, tvHikeName, tvHikeLocation, tvHikeLevel, tvHikeLength, tvHikeDate;
        LinearLayout mainHikeItemLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeId = itemView.findViewById(R.id.tvHikeId);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeLocation = itemView.findViewById(R.id.tvHikeLocation);
            tvHikeLevel = itemView.findViewById(R.id.tvHikeLevel);
            tvHikeLength = itemView.findViewById(R.id.tvHikeLength);
            tvHikeDate = itemView.findViewById(R.id.tvHikeDate);
            mainHikeItemLayout = itemView.findViewById(R.id.mainHikeItemLayout);
        }
    }


}
