package com.hmt.weather.activittis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hmt.weather.R;
import com.hmt.weather.adapters.FutureAdapter;
import com.hmt.weather.entity.FutureDay;

import java.util.ArrayList;

public class FutureActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterTomorrow;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        setVariable();
        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<FutureDay> items = new ArrayList<>();
        items.add(new FutureDay("Sat", "storm", "Storm", 24, 12));
        items.add(new FutureDay("Sun", "cloudy", "Cloudy", 25, 16));
        items.add(new FutureDay("Mon", "windy", "Windy", 29, 15));
        items.add(new FutureDay("Tue", "cloudy_sunny", "Cloudy Sunny", 23, 11));
        items.add(new FutureDay("Wen", "sunny", "Sunny", 28, 11));
        items.add(new FutureDay("Thu", "rainny", "Rainy", 23, 12));

        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTomorrow = new FutureAdapter(items);
        recyclerView.setAdapter(adapterTomorrow);

    }

    private void setVariable() {
        ConstraintLayout backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FutureActivity.this, MainActivity.class));
            }
        });
    }
}