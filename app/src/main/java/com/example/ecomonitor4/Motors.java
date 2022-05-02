package com.example.ecomonitor4;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.app.Activity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Motors extends Activity {

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    String command = "hello";

    Button forward;
    Button stopButton;
    Button back;
    Button left;
    Button right;

    Handler handler = new Handler();

    Button stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motors);

        forward = findViewById(R.id.forwardButton);
        forward.setOnClickListener(v -> command = "r");

        left = findViewById(R.id.leftButton);
        left.setOnClickListener(v -> command = "b");

        right = findViewById(R.id.rightButton);
        right.setOnClickListener(v -> command = "f");

        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> command = "l");

        stopButton = findViewById(R.id.stopMotorsButton);
        stopButton.setOnClickListener(v -> command = "s");

        stop = findViewById(R.id.exitMotorsButton);
        stop.setOnClickListener(v -> {
            handler.removeCallbacks(runnableCode);
            this.finish();
        });

        runnableCode.run();

    }

    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            sendBtMsg(command);
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(Motors.this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Motors.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
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

            System.out.println(msg2send);
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg2send.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
