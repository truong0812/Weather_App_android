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
import com.hmt.weather.entity.FutureDay;

import java.util.ArrayList;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.viewholder> {

    ArrayList<FutureDay> items;
    Context context;

    public FutureAdapter(ArrayList<FutureDay> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FutureAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_future, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapter.viewholder holder, int position) {
        FutureDay futureDay = items.get(position);

        holder.tvDay.setText(futureDay.getDay());
        holder.tvLow.setText(futureDay.getLowTemp() +"°");
        holder.tvHigh.setText(futureDay.getHighTemp() + "°");
        holder.tvStatus.setText(futureDay.getStatus());

        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(futureDay.getPicPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.ivPic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView tvDay, tvStatus, tvHigh, tvLow;
        ImageView ivPic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.TVDay);
            tvStatus = itemView.findViewById(R.id.TVStatus);
            tvHigh = itemView.findViewById(R.id.TVHigh);
            tvLow = itemView.findViewById(R.id.TVLow);
            ivPic = itemView.findViewById(R.id.IVPic);




        }
    }
}
