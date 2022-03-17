package com.example.ecomonitor4;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;


public class WeatherAPI {

    public static int[] getTemperature(String city, String key) throws IOException {
        JsonArray d = (JsonArray) getData.getData(city, key).get("list");
        int length = d.size();
        int[] output = new int[length];
        int x = 0;
        for (int i = 0; i < length; i++) {
            JsonObject whatever = (JsonObject) d.remove(0);
            JsonObject main = (JsonObject) whatever.get("main");
            int temp = main.get("temp").getAsInt();
            System.out.println(whatever.toString());
            output[x] = (temp - 273);
            x++;
        }
        return output;
    }
    public static float[] getPrecipitation(String city, String key) throws IOException {
        JsonArray d = (JsonArray) getData.getData(city, key).get("list");
        int length = d.size();
        float[] output = new float[length];
        int x = 0;
        for (int i = 0; i < length; i++) {
            JsonObject whatever  = (JsonObject) d.remove(0);
            output[x] = whatever.get("pop").getAsFloat();
            x++;
        }
        return output;
    }
    public static int[] getHumidity(String city, String key) throws IOException {
        JsonArray d = (JsonArray) getData.getData(city, key).get("list");
        int length = d.size();
        int[] output = new int[length];
        int x = 0;
        for (int i = 0; i < length; i++) {
            JsonObject whatever = (JsonObject) d.remove(0);
            JsonObject main = (JsonObject) whatever.get("main");
            int humidity = main.get("humidity").getAsInt();
            output[x] = humidity;
            x++;
        }
        return output;
    }

}
