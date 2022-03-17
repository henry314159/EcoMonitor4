package com.example.ecomonitor4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Weather extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button weatherButton = findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(v -> {
            EditText city = findViewById(R.id.editTextWeather);

            Intent data = new Intent(getApplicationContext(), WeatherGraphs.class);
            data.putExtra("com.example.ecomonitor4.CITY", city.getText().toString());
            startActivity(data);
        });
    }

}
