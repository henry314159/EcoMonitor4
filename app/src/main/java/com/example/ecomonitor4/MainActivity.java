package com.example.ecomonitor4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Make pi collect data ** probably gonna be very hard i.e. lots of research **
// Automatically store pi data in a user selected field + display when field selected + make field system more elegant
// Weather 'city not found'
// Fix final naming of app
// Upload code to github - best practices for making code available online?

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button weatherButton = findViewById(R.id.weatherBtn);
        Button piButton = findViewById(R.id.piBtn);
        Button fieldButton = findViewById(R.id.fieldsBtn);

        weatherButton.setOnClickListener(v -> {
            Intent data = new Intent(getApplicationContext(), Weather.class);
            startActivity(data);
        });

        piButton.setOnClickListener(v -> {
            Intent data = new Intent(getApplicationContext(), PiBluetooth.class);
            startActivity(data);
        });

        fieldButton.setOnClickListener(v -> {
            Intent data = new Intent(getApplicationContext(), Fields.class);
            startActivity(data);
        });
    }
}
