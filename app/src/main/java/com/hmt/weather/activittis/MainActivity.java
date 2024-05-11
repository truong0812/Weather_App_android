package com.hmt.weather.activittis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hmt.weather.R;
import com.hmt.weather.adapters.HourlyAdapter;
import com.hmt.weather.entity.Hourly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    private LocationManager locationManager;
    private static final int PERMISSION_CODE = 100;
    private String cityName = "HaNoi";
    private ContentLoadingProgressBar loadingProgressBar;
    private View grayView;
    private TextView tvWeather, tvDay, tvTemp, tvMaxMin, tvRainable, tvWindSpeed, tvHumidity;
    private ImageView imageView;
    private ArrayList<Hourly> items;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initial();
        initRecyclerView();
        setVariable();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            // Quyền truy cập vị trí đã được cấp, tiến hành lấy thông tin địa chỉ
            getCurrentLocation();
        }

        getWeatherInfo(cityName);

    }

    private void initial() {
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        grayView = findViewById(R.id.grayOverlay);
        tvDay = findViewById(R.id.TVDay);
        tvHumidity = findViewById(R.id.TVHumidity);
        tvTemp = findViewById(R.id.TVtemp);
        tvRainable = findViewById(R.id.TVRainable);
        tvWeather = findViewById(R.id.TVweather);
        tvMaxMin = findViewById(R.id.TVmaxmin);
        tvWindSpeed = findViewById(R.id.TVWindspeed);
        imageView = findViewById(R.id.ImageView);
    }



    private void setVariable() {
        TextView nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FutureActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập vị trí đã được cấp, tiến hành lấy thông tin địa chỉ
                getCurrentLocation();
            } else {
                // Người dùng từ chối cấp quyền, xử lý tùy ý
            }
        }
    }


    private void initRecyclerView() {
        items = new ArrayList<>();

        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterHourly = new HourlyAdapter(items);

        recyclerView.setAdapter(adapterHourly);
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses != null && addresses.size() > 0) {
                                        // Lấy thông tin địa chỉ từ danh sách địa chỉ trả về
                                        Address address = addresses.get(0);
                                        String cityName = address.getLocality();
                                        Toast.makeText(MainActivity.this, "Current city: " + cityName, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Không tìm thấy địa chỉ
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            // Quyền truy cập vị trí chưa được cấp, yêu cầu quyền truy cập
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }
    }





    private void getWeatherInfo(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=21e5eae2935d471c9fb92707241604&q="
                + cityName +"&days=7&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingProgressBar.setVisibility(View.GONE);
                        grayView.setVisibility(View.GONE);
                        items.clear();

                        try {
                            tvTemp.setText(response.getJSONObject("current").getString("temp_c"));

                            String weather = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            tvWeather.setText(weather);

                            String inputDay = response.getJSONObject("current").getString("last_updated");
                            LocalDateTime dateTime = null;
                            String outputDay = "";
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                dateTime = LocalDateTime.parse(inputDay, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                                outputDay = dateTime.format(DateTimeFormatter.ofPattern("EEE MMM dd | hh:mm a", Locale.ENGLISH));
                            }

                            tvDay.setText(outputDay);

                            tvHumidity.setText(response.getJSONObject("current").getString("humidity") + "%");

                            tvWindSpeed.setText(response.getJSONObject("current").getString("wind_kph") + " km/h");

                            JSONObject today = (JSONObject) response.getJSONObject("forecast").getJSONArray("forecastday").get(0);
                            JSONObject data = today.getJSONObject("day");

                            String highTemp = data.getString("maxtemp_c") + "°C";
                            String lowTemp = data.getString("mintemp_c") + "°C";
                            tvMaxMin.setText("High: "+ highTemp + " | " + "Low: " + lowTemp);

                            tvRainable.setText(data.getString("daily_chance_of_rain" )+ "%");

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

                            JSONArray hourArray = today.getJSONArray("hour");
                            for(int i = 0; i < hourArray.length(); i++) {

                                JSONObject hourJson = (JSONObject) hourArray.get(i);

                                String[] time = hourJson.getString("time").split(" ");
                                String hour = time[1];

                                String temp = hourJson.getString("temp_c");
                                String weatherhour = hourJson.getJSONObject("condition").getString("text");

                                String pic ="cloudy";

                                if(weatherhour.contains("cloudy") && weatherhour.contains("sunny")) {
                                    pic = "cloudy_sunny";
                                }
                                if(weatherhour.contains("rain")) {
                                    pic = "rain";
                                }
                                if(weatherhour.contains("sunny")) {
                                    pic = "sunny";
                                }
                                if(weatherhour.contains("snow")) {
                                    pic = "snowy";
                                }
                                if(weatherhour.contains("storm")) {
                                    pic = "storm";
                                }
                                if(weatherhour.contains("wind")) {
                                    pic = "wind";
                                }

                                items.add(new Hourly(hour, temp, pic));
                            }

                            adapterHourly.notifyDataSetChanged();


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