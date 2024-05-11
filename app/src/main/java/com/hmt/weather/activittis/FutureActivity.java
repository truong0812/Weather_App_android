package com.hmt.weather.activittis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hmt.weather.R;
import com.hmt.weather.adapters.FutureAdapter;
import com.hmt.weather.entity.FutureDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FutureActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterTomorrow;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView tvWeather, tvTemp,  tvRainable, tvWindSpeed, tvHumidity;
    private String cityName = "HaNoi";

    private ContentLoadingProgressBar loadingProgressBar;
    private View grayView;

    private ArrayList<FutureDay> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        initial();
        setVariable();
        initRecyclerView();
        getWeatherInfo(cityName);
    }

    private void initial() {
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        grayView = findViewById(R.id.grayOverlay);
        tvHumidity = findViewById(R.id.TVHumidity);
        tvTemp = findViewById(R.id.TVtemp);
        tvRainable = findViewById(R.id.TVRainable);
        tvWeather = findViewById(R.id.TVweather);
        tvWindSpeed = findViewById(R.id.TVWindspeed);
        imageView = findViewById(R.id.imageView);
    }

    private void initRecyclerView() {
        items = new ArrayList<>();


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

    private void getWeatherInfo(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=21e5eae2935d471c9fb92707241604&q="
                + cityName +"&days=8&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(FutureActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingProgressBar.setVisibility(View.GONE);
                        grayView.setVisibility(View.GONE);
                        items.clear();

                        try {
                            JSONArray next7day = response.getJSONObject("forecast").getJSONArray("forecastday");
                            JSONObject tomorrow1 = (JSONObject) next7day.get(1);
                            JSONObject tomorrow = tomorrow1.getJSONObject("day");

                            tvTemp.setText(tomorrow.getString("avgtemp_c"));


                            String weather = tomorrow.getJSONObject("condition").getString("text");
                            tvWeather.setText(weather);


                            tvHumidity.setText(tomorrow.getString("avghumidity") + "%");

                            tvWindSpeed.setText(tomorrow.getString("maxwind_kph") + " km/h");

                            tvRainable.setText(tomorrow.getString("daily_chance_of_rain" )+ "%");

                            weather = weather.toLowerCase();

                            imageView.setImageResource(R.drawable.cloudy);

                            if(weather.contains("cloudy") && weather.contains("sunny")) {
                                imageView.setImageResource(R.drawable.cloudy_sunny);
                            }
                            if(weather.contains("rain")) {
                                imageView.setImageResource(R.drawable.rainy);
                            }
                            if(weather.contains("sunny")) {
                                imageView.setImageResource(R.drawable.sunny);
                            }
                            if(weather.contains("snow")) {
                                imageView.setImageResource(R.drawable.snowy);
                            }
                            if(weather.contains("storm")) {
                                imageView.setImageResource(R.drawable.storm);
                            }
                            if(weather.contains("wind")) {
                                imageView.setImageResource(R.drawable.windy);
                            }


                            for(int i = 2; i < next7day.length(); i++) {

                                JSONObject nextday1 = (JSONObject) next7day.get(i);
                                JSONObject nextday = nextday1.getJSONObject("day");

                                String input = nextday1.getString("date");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate localDate = LocalDate.parse(input, formatter);
                                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                                String date = dayOfWeek.toString().substring(0,3);

                                String status = nextday.getJSONObject("condition").getString("text");
                                String pic = "";

                                pic = "cloudy";
                                if(status.contains("cloudy") && status.contains("sunny")) {
                                    pic = "cloudy_sunny";
                                }
                                if(status.contains("rain")) {
                                    pic = "rainy";
                                }
                                if(status.contains("sunny")) {
                                    pic = "sunny";
                                }
                                if(status.contains("snow")) {
                                    pic = "snowy";
                                }
                                if(status.contains("storm")) {
                                    pic = "storm";
                                }
                                if(status.contains("wind")) {
                                    pic = "windy";
                                }

                                String highTemp = nextday.getString("maxtemp_c");
                                String lowTemp = nextday.getString("mintemp_c");

                                items.add(new FutureDay(date, pic, status, highTemp, lowTemp));
                            }

                            adapterTomorrow.notifyDataSetChanged();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(error.getMessage(), error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}