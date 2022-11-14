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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    private final static int CONNECTION_STATUS = 1;
    private final static int READ_MESSAGE = 2;

    BottomNavigationView bottomNavigationMenu;

    //Instansiasi data dari arduino
    static final String EXTRA_DATA_LVL = "extra_data_lvl";
    static final String EXTRA_DATA_FUEL = "extra_data_fuel";
    static final String EXTRA_DATA_AFR = "extra_data_afr";
    static final String EXTRA_DATA_VOLT = "extra_data_volt";
    static final String EXTRA_DATA_RPM = "extra_data_rpm";
    static final String EXTRA_DATA_FREQ = "extra_data_freq";
    
    private static float lvl = 0;
    private static float fuel = 0f;
    private static float afr = 0f;
    private static float volt = 0f;
    private static float rpm = 0;
    private static float freq = 0f;



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


        //Instansiasi Bottom Navbar
        bottomNavigationMenu = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationMenu.setSelectedItemId(R.id.menumode);

        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menumode:
                        return true;
                    case R.id.menuhistory:
                        Intent intent = new Intent(new Intent(getApplicationContext(), Telemetry.class));
                        intent.putExtra(MainActivity.EXTRA_DATA_LVL,lvl);
                        intent.putExtra(MainActivity.EXTRA_DATA_FUEL,fuel);
                        intent.putExtra(MainActivity.EXTRA_DATA_AFR,afr);
                        intent.putExtra(MainActivity.EXTRA_DATA_VOLT,volt);
                        intent.putExtra(MainActivity.EXTRA_DATA_RPM,rpm);
                        intent.putExtra(MainActivity.EXTRA_DATA_FREQ,freq);

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

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
            textBluetoothStatus.setText("Connecting");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
        }

        //Handler
        handler = new Handler(Looper.getMainLooper()) {

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

                    //apabila message berisi data dari Arduino
                    case READ_MESSAGE:
                        String statusText = msg.obj.toString();
                        String[] parts = statusText.split(Pattern.quote("-"));

                        String part1 = parts[0];
                        String part2 = parts[1];
                        String part3 = parts[2];
                        String part4 = parts[3];
                        String part5 = parts[4];
                        String part6 = parts[5];

                        lvl = Float.parseFloat(part1);
                        fuel = Float.parseFloat(part2);
                        afr = Float.parseFloat(part3);
                        volt = Float.parseFloat(part4);
                        rpm = Float.parseFloat(part5);
                        freq= Float.parseFloat(part6);
                        break;
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
            String androidCmd = "w";
            connectedThread.write(androidCmd);
            displayCurrentMode.setText("ECO MODE");
        });

        //Sporty Button
        sportButton.setOnClickListener(view -> {
            String androidCmd = "s";
            connectedThread.write(androidCmd);
            displayCurrentMode.setText("SPORTY MODE");
        });

        //Default Button
        defaultButton.setOnClickListener(view -> {
            String androidCmd = "d";
            connectedThread.write(androidCmd);
            displayCurrentMode.setText("SPORTY MODE");
        });
//        ecoButton.setOnClickListener(v -> {
//            //log ecoButton
//            Log.i("Eco Button State", "Eco Mode Terpilih");
//        });
//
//        //Function sportButton
//        sportButton.setOnClickListener(v -> {
//            //log sportButton
//            Log.i("Sporty Button State", "Sporty Mode Terpilih");
//        });
//
//        //Function sportButton
//        defaultButton.setOnClickListener(v -> {
//            //log sportButton
//            Log.i("Default Button State", "Default Mode Terpilih");
//        });
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

        public void write(String input) {
            byte[] bytes = input.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }
    }
}