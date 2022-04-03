package com.example.ecomonitor4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import de.nitri.gauge.Gauge;

public class PiBluetooth extends Activity {

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    Handler handler = new Handler();

    Button startStop;
    boolean onOff = true;

    Gauge pressure;
    Gauge temp;
    Gauge humid;
    TextView moisture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi);

        startStop = findViewById(R.id.testButton);

        startStop.setOnClickListener(v -> {
            if (onOff) {
                runnableCode.run();
                onOff = false;
            } else {
                handler.removeCallbacks(runnableCode);
                onOff = true;
            }
        });

        pressure = findViewById(R.id.pressureGauge);
        temp = findViewById(R.id.tempGauge);
        humid = findViewById(R.id.humidGauge);
        moisture = findViewById(R.id.moisture);
    }

    private final Runnable runnableCode = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void run() {
            sendBtMsg("hello");
            while (!Thread.currentThread().isInterrupted()) {
                int bytesAvailable;
                boolean workDone = false;

                try {

                    final InputStream mmInputStream;

                    mmInputStream = mmSocket.getInputStream();

                    bytesAvailable = mmInputStream.available();
                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];
                        Log.e("EcoMonitor recv bt", "bytes available");
                        byte[] readBuffer = new byte[1024];
                        mmInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, StandardCharsets.US_ASCII);
                                System.out.println(data);
                                readBufferPosition = 0;

                                handler.post(() -> {
                                    String[] splitData = data.split(" ");
                                    float tempVal = Float.parseFloat(splitData[0]);
                                    temp.moveToValue(tempVal);
                                    float pressureVal = Float.parseFloat(splitData[1]);
                                    pressure.moveToValue(pressureVal / 100);
                                    float humidVal = Float.parseFloat(splitData[2]);
                                    humid.moveToValue(humidVal);
                                    moisture.setText("Moisture: " + splitData[3]);
                                });
                                workDone = true;
                                break;

                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                        if (workDone) {
                            mmSocket.close();
                            break;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(PiBluetooth.this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PiBluetooth.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    public void sendBtMsg(String msg2send) {
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("raspberrypi-0"))
                    {
                        Log.e("EcoMonitor",device.getName());
                        mmDevice = device;
                        break;
                    }
                }
            }
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }

            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg2send.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
