package com.example.newui;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private String deviceAddress = null;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static CreateConnectThread createConnectThread;
    public static ConnectedThread connectedThread;

    //Bluetooth message status
    private final static int CONNECTION_STATUS = 1;
    private final static int READ_MESSAGE = 2;

    private static float adc;
    private static float fuel;
    private static float afr;
    private static float rpm;
    private static float freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instansiasi UI
        final TextView textBluetoothStatus = findViewById(R.id.textBluetoothStatus);
        final TextView displayCurrentMode = findViewById(R.id.displayCurrentMode);
        Button connectButton = findViewById(R.id.connectButton);
        Button disconnectButton = findViewById(R.id.disconnectButton);
        LinearLayout ecoButton = findViewById(R.id.ecoButton);
        LinearLayout sportButton = findViewById(R.id.sportButton);
        LinearLayout defaultButton = findViewById(R.id.defaultButton);

        TextView displayFuel = findViewById(R.id.textFuelValue);
        TextView displayAdc = findViewById(R.id.textAdcValue);
        TextView displayAfr = findViewById(R.id.textAfrValue);
        TextView displayRpm = findViewById(R.id.textRpmValue);
        TextView displayFreq = findViewById(R.id.textFreqValue);


        //Connect Button
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                startActivity(intent);
            }
        });

        //Get informasi Device Address
        deviceAddress = getIntent().getStringExtra("deviceAddress");

        //Ketika Device Address telah ditemukan
        if (deviceAddress != null) {
            textBluetoothStatus.setText("Connecting...");
            //Ketika "deviceAddress" ditemukan, kode akan membuat connection thread untuk menyambungkan perangkat android ke bluetooth module
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
        }

        //Handler untuk mengupdate UI ketika Thread mengeluarkan output baru dan mengoper value ke Main Thread
        handler = new Handler(Looper.getMainLooper()) {

            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTION_STATUS:
                        switch (msg.arg1) {
                            case 1:
                                textBluetoothStatus.setText("Bluetooth Connected");
                                break;
                            case -1:
                                textBluetoothStatus.setText("Connection Failed");
                                break;
                        }
                        break;

                    //Parsing arduino data
                    case READ_MESSAGE:
//                        for (;;) {
                            String statusText = msg.obj.toString();
                            String[] parts = statusText.split(Pattern.quote("-"));

                            String sadc = parts[0];
//                            String sfuel = parts[1];
//                            String safr = parts[2];
                            String srpm = parts[3];
                            String sfreq = parts[4];

//                            adc = Float.parseFloat(parts[0]);
                            fuel = Float.parseFloat(parts[1]);
                            afr = Float.parseFloat(parts[2]);
//                            rpm = Float.parseFloat(parts[3]);
//                            freq= Float.parseFloat(parts[4]);

                            displayAdc.setText(sadc);
                            displayFuel.setText(Float.toString(fuel));
                            displayAfr.setText(Float.toString(afr));
                            displayRpm.setText(srpm);
                            displayFreq.setText(sfreq);

                            Log.i("data", statusText);
                            break;
//                        }
                }
            }
        };

        //disconnect
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createConnectThread != null) {
                    createConnectThread.cancel();
                    textBluetoothStatus.setText("Bluetooth Disconnected");
                }
            }
        });

        //Eco Button
        ecoButton.setOnClickListener(view -> {
//            if (CONNECTION_STATUS == 1) {
                String androidCmd = "e";
                connectedThread.write(androidCmd);
                displayCurrentMode.setText("ECO MODE");
//            } else {
//                Toast.makeText(MainActivity.this,
//                        "Bluetooth is not connected!", Toast.LENGTH_SHORT).show();
//            }
        });

        //Sporty Button
        sportButton.setOnClickListener(view -> {
//            if (CONNECTION_STATUS == 1) {
                String androidCmd = "s";
                connectedThread.write(androidCmd);
                displayCurrentMode.setText("SPORT MODE");
//            } else {
//                Toast.makeText(MainActivity.this,
//                        "Bluetooth is not connected!", Toast.LENGTH_SHORT).show();
//            }
        });

        //Default Button
        defaultButton.setOnClickListener(view -> {
//            if (CONNECTION_STATUS == 1) {
                String androidCmd = "d";
                connectedThread.write(androidCmd);
                displayCurrentMode.setText("DEFAULT MODE");
//            } else {
//                Toast.makeText(MainActivity.this,
//                        "Bluetooth is not connected!", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    //Establishing Bluetooth Connection Thread
    public static class CreateConnectThread<connectedThread> extends Thread {

        @SuppressLint("MissingPermission")
        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            @SuppressLint("MissingPermission") UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
            try {
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e("Error Message", e.toString());
            }
            mmSocket = tmp;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                handler.obtainMessage(CONNECTION_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                    handler.obtainMessage(CONNECTION_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                }
                return;
            }

            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    //Data Transfer Thread
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes = 0;
            while (true) {
                try {
                    buffer[bytes] = (byte) mmInStream.read();
                    String arduinoMsg = null;
                    if (buffer[bytes] == '\n') {
                        arduinoMsg = new String(buffer, 0, bytes);
                        handler.obtainMessage(READ_MESSAGE, arduinoMsg).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Mengirim androidcmd ke STM32
        public void write(String input) {
            //Mengubah String menjadi bytes
            byte[] bytes = input.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }
    }
}