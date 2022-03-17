package com.example.ecomonitor4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Fields extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields);

        Button addNewField = findViewById(R.id.addNewFieldBtn);
        addNewField.setOnClickListener(v -> {
            Intent data = new Intent(getApplicationContext(), AddField.class);
            startActivity(data);
            this.finish();
        });

        String fieldInfo = readFile();
        String[] fieldInfoSplit = fieldInfo.split(";");

        ListView listView = findViewById(R.id.listView);
        if (!fieldInfo.isEmpty()) {
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fieldInfoSplit));
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent data = new Intent(getApplicationContext(), Field.class);
            data.putExtra("com.example.ecomonitor4.FIELD_INFO", fieldInfoSplit[position]);
            data.putExtra("com.example.ecomonitor4.POSITION", position);
            startActivity(data);
            this.finish();
        });
    }

    private String readFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = openFileInput("MyFile");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String temp;
            while ((temp = reader.readLine()) != null) {
                stringBuilder.append(temp);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
