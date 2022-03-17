package com.example.ecomonitor4;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddField extends AppCompatActivity {

    EditText data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        data = findViewById(R.id.fieldInfoInput);
        Button newFieldBtn = findViewById(R.id.confirmNewFieldBtn);

        newFieldBtn.setOnClickListener(v -> writeData());
    }
    private void writeData() {
        try {
            String currentData = readFile();
            FileOutputStream fileOutputStream = openFileOutput("MyFile", Context.MODE_PRIVATE);
            fileOutputStream.write((currentData+data.getText().toString()+";").getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.finish();
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
