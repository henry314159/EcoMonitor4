package com.example.ecomonitor4;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView hyperlinkText = findViewById(R.id.descriptionText);
        hyperlinkText.setMovementMethod(LinkMovementMethod.getInstance());

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
