package com.kai.bluetoothcontroller;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    Button btnUp, btnDown, btnLeft, btnRight;
    TextView txtStatus;

    Bluetooth bt;
    final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        btnLeft = findViewById(R.id.left);
        btnRight = findViewById(R.id.right);
        txtStatus = findViewById(R.id.status);

        buttonEvent(btnUp);
        buttonEvent(btnDown);
        buttonEvent(btnLeft);
        buttonEvent(btnRight);

        bt = new Bluetooth(this, mHandler);
        connectService();
    }

    private void buttonEvent(Button button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch(v.getId()) {
                        case R.id.up:
                            Log.i(TAG, "send 1 for up");
                            bt.sendMessage(1);
                            break;
                        case R.id.down:
                            Log.i(TAG, "send 2 for down");
                            bt.sendMessage(2);
                            break;
                        case R.id.left:
                            Log.i(TAG, "send 3 for left");
                            bt.sendMessage(3);
                            break;
                        case R.id.right:
                            Log.e(TAG, "send 4 for right");
                            bt.sendMessage(4);
                            break;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e(TAG, "send 5 for button release");
                    bt.sendMessage(5);
                }
                return true;
            }
        });
    }

    public void connectService(){
        try {
            txtStatus.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d("main", "Btservice started - listening");
                txtStatus.setText("Connected");
            } else {
                Log.w("main", "Btservice started - bluetooth is not enabled");
                txtStatus.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e("main", "Unable to start bt ",e);
            txtStatus.setText("Unable to connect " +e);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };
}
