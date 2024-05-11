package com.hmt.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hmt.weather.R;
import com.hmt.weather.entity.Hourly;


import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.viewholder> {

    ArrayList<Hourly> item;
    Context context;

    public HourlyAdapter(ArrayList<Hourly> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public HourlyAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.viewholder holder, int position) {
        Hourly hourly = item.get(position);

        holder.tvHour.setText(hourly.getHour());
        holder.tvTemp.setText(hourly.getTemp() + "°");
        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(hourly.getPicPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView tvHour, tvTemp;
        ImageView ivPic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvHour =itemView.findViewById(R.id.TVHour);
            tvTemp =itemView.findViewById(R.id.TVtemp);
            ivPic =itemView.findViewById(R.id.IVPic);
        }
    }
}
