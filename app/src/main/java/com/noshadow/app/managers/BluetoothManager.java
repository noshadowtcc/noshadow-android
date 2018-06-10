package com.noshadow.app.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.noshadow.app.model.BluetoothConnected;
import com.noshadow.app.model.JobUpdate;
import com.noshadow.app.model.LocationJob;
import com.noshadow.app.model.LocationPayload;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothManager extends BaseManager implements Bluetooth.CommunicationCallback {

    private Bluetooth bt;
    private List<BluetoothDevice> paired;
    private String name;
    private boolean registered = false;
    private String deviceName = "";

    public BluetoothManager(Context context) {
        _context = context;

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);
        registered = true;
    }

    public void connectBluetooth(Activity activity) {
        bt = new Bluetooth(activity);
        bt.enableBluetooth();

        bt.setCommunicationCallback(this);
        int i = 0;
        for (BluetoothDevice device : bt.getPairedDevices()) {
            if (device.getName().equals("BT-DR-TCC")) {
                name = bt.getPairedDevices().get(i).getName();

                bt.connectToDevice(bt.getPairedDevices().get(i));

                IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                _context.registerReceiver(mReceiver, filter);
//                EventBus.getDefault().post(new BluetoothConnected(device.getName()));
                registered = true;
                return;
            }
            i++;
        }
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        deviceName = device.getName();
        EventBus.getDefault().post(new BluetoothConnected(device.getName()));
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        EventBus.getDefault().post(new BluetoothConnected(""));
    }

    @Override
    public void onMessage(String message) {

        if(!message.startsWith("$PUBX"))
            return;
        //$PUBX,00,173054.00,2328.63862,S,04726.11275,W,573.736,G3,42,44,0.589,300.46,-0.060,,2.43,4.18,3.25,5,0,0*4C

        String[] parts = message.split(",");

        String signalLatitude = parts[4].equals("S") ? "-" : "";
        String signalLongitude = parts[6].equals("W") ? "-" : "";

        String fullLatitude = parts[3];
        String fullLongitude = parts[5];

        String keepLat = fullLatitude.substring(0,2);
        String keepLon = fullLongitude.substring(0,3);

        String partLat = fullLatitude.substring(2,fullLatitude.length()-1).replace(".", "");
        String partLon = fullLongitude.substring(3,fullLongitude.length()-1).replace(".", "");

        int partLatInt = (int)(Integer.parseInt(partLat)/0.6);
        int partLonInt = (int)(Integer.parseInt(partLon)/0.6);

        if(keepLat.startsWith("0")){
            keepLat = keepLat.substring(1);
        }

        if(keepLon.startsWith("0")){
            keepLon = keepLon.substring(1);
        }

        String latitude = signalLatitude + keepLat + "." + Integer.toString(partLatInt);
        String longitude = signalLongitude + keepLon + "." + Integer.toString(partLonInt);
        String speed  = parts[11];
        String height  = parts[7];

        Log.e("NOSHADOW", "Location changed: (" + latitude + "," + longitude + ")");

        LocationPayload payload = new LocationPayload();

        payload.setDeviceId(Settings.Secure.getString(_context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        payload.setLatitude(latitude);
        payload.setLongitude(longitude);
        payload.setSpeed(speed);
        payload.setHeight(height);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        payload.setLogDate(format.format(new Date()));


        ManagerHelper.getInstance()
                .getNetworkJobManager()
                .addJobInBackground(
                        new LocationJob(payload,
                                UUID.randomUUID(),
                                 latitude + "," + longitude,
                                deviceName));

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:

                        Toast.makeText(context, "Turn on bluetooth", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        getDevices();
                        break;
                }
            }
        }
    };

    private void getDevices() {

        paired = bt.getPairedDevices();

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired) {
            names.add(d.getName());
        }
    }
}

