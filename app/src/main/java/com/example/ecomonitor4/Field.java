package com.example.ecomonitor4;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Field extends AppCompatActivity {

    int pos;
    int pressed = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        String name = "";

        if (getIntent().hasExtra("com.example.ecomonitor4.FIELD_INFO")) {
            name = getIntent().getStringExtra("com.example.ecomonitor4.FIELD_INFO");
        }

        if (getIntent().hasExtra("com.example.ecomonitor4.POSITION")) {
            pos = getIntent().getIntExtra("com.example.ecomonitor4.POSITION", 0);
        }
        
        TextView item = findViewById(R.id.itemName);
        item.setText(name);

        Button delete = findViewById(R.id.deleteFieldBtn);
        delete.setOnClickListener(v -> {
            if (pressed == 2) {
                deleteItem();
                this.finish();
            } else {
                pressed ++;
            }
        });

        Button dataButton = findViewById(R.id.fieldInfoButton);
        dataButton.setOnClickListener(v -> {
            Intent data = new Intent(getApplicationContext(), FieldInfoAndGraphs.class);
            startActivity(data);
        });
    }

    private void writeData(String data) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("MyFile", Context.MODE_PRIVATE);
            fileOutputStream.write((data).getBytes());
            fileOutputStream.close();
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteItem() {
        String currentData = readFile();
        String[] cDArray = currentData.split(";");
        String[] newDataArray = new String[cDArray.length-1];
        int x = 0;
        int i = 0;
        while (x < newDataArray.length) {
            if (i != pos) {
                newDataArray[x] = cDArray[i];
                x ++;
            }
            i ++;
        }
        String temp = String.join(";", newDataArray)+";";
        if (temp.equals(";")) {
            writeData("");
            return;
        }
        writeData(temp);
    }
}
