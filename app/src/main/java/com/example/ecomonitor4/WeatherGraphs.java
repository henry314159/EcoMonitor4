package com.example.ecomonitor4;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.Objects;

public class WeatherGraphs extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_graphs);

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert ai != null;
        Bundle bundle = ai.metaData;
        String myApiKey = bundle.getString("weatherKey");

        new Thread(() -> {
            Looper.prepare();

            TextView tempText = findViewById(R.id.tempGraphText);
            TextView popText = findViewById(R.id.popGraphText);
            TextView humidText = findViewById(R.id.humidityGraphText);
            tempText.setText(getString(R.string.temp_text));
            popText.setText(getString(R.string.pop_text));
            humidText.setText(getString(R.string.humid_text));
            if (getIntent().hasExtra("com.example.ecomonitor4.CITY")) {
                int[] output1 = new int[0];
                try {
                    String city = getIntent().getStringExtra("com.example.ecomonitor4.CITY");

                    output1 = WeatherAPI.getTemperature(city, myApiKey);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.finish();
                }
                GraphView graphTemp = findViewById(R.id.graphTemp);
                LineGraphSeries<DataPoint> series1;

                series1 = new LineGraphSeries<>();
                for (int i = 0; i < Objects.requireNonNull(output1).length; i++) {
                    series1.appendData(new DataPoint(i*3, output1[i]), true, 100);
                }
                graphTemp.addSeries(series1);

                float[] output2 = new float[0];
                try {
                    String city = getIntent().getStringExtra("com.example.ecomonitor4.CITY");
                    output2 = WeatherAPI.getPrecipitation(city, myApiKey);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.finish();
                }
                GraphView graphPOP = findViewById(R.id.graphPOP);
                LineGraphSeries<DataPoint> series2;

                series2 = new LineGraphSeries<>();

                for (int i = 0; i < Objects.requireNonNull(output2).length; i++) {
                    series2.appendData(new DataPoint(i*3, output2[i] * 100), true, 100);
                }
                graphPOP.addSeries(series2);

                int[] output3 = new int[0];
                try {
                    String city = getIntent().getStringExtra("com.example.ecomonitor4.CITY");
                    output3 = WeatherAPI.getHumidity(city, myApiKey);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.finish();
                }
                GraphView graphHumidity = findViewById(R.id.graphHumidity);
                LineGraphSeries<DataPoint> series3;

                series3 = new LineGraphSeries<>();

                for (int i = 0; i < Objects.requireNonNull(output3).length; i++) {
                    series3.appendData(new DataPoint(i*3, output3[i]), true, 100);
                }
                graphHumidity.addSeries(series3);
            }
        }).start();
    }
}