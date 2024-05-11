package com.hmt.weather.activittis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hmt.weather.R;
import com.hmt.weather.adapters.HourlyAdapter;
import com.hmt.weather.entity.Hourly;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<Hourly> items = new ArrayList<>();

        items.add(new Hourly("9pm", 28, "cloudy"));
        items.add(new Hourly("10pm", 29, "sunny"));
        items.add(new Hourly("11pm", 30, "wind"));
        items.add(new Hourly("12pm", 31, "rainy"));
        items.add(new Hourly("1pm", 32, "storm"));

        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,  false));
        adapterHourly = new HourlyAdapter(items);

        recyclerView.setAdapter(adapterHourly);
    }
}