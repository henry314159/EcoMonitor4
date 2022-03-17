package com.example.ecomonitor4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class getData {
    public static JsonObject getData(String query, String key) throws IOException {
        Gson gson = new Gson();
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + query + "&APPID=" + key;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert con != null;
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return gson.fromJson(new BufferedReader(new InputStreamReader(con.getInputStream())), JsonObject.class);
    }
}