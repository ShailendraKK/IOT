package com.example.androidbtcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MoistureSensor extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    protected static final int RESULT_SPEECH = 2;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<BluetoothDevice> pairedDeviceArrayList;

    //    TextView textInfo, textStatus, textByteCnt;
    ListView listViewPairedDevice;
   // LinearLayout inputPane;
   // EditText inputField;
   Button btnmoist;
    Button btnmoist1;
    TextView status;
    TextView status1;
   String last_stage = "LOW";
    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moisture);

        // textInfo = (TextView)findViewById(R.id.info);
        listViewPairedDevice = (ListView)findViewById(R.id.pairedlist1);

      //  inputPane = (LinearLayout)findViewById(R.id.inputpane);
        //  inputField = (EditText)findViewById(R.id.input);

            btnmoist=(Button)findViewById(R.id.btn_moist);
        btnmoist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(myThreadConnected!=null){
                    byte[] bytesToSend = "3".getBytes();
                    myThreadConnected.write(bytesToSend);
                    byte[] NewLine = "\n".getBytes();
                    myThreadConnected.write(NewLine);
                }
            }});
        status1=(TextView)findViewById(R.id.status1_1);
        btnmoist1=(Button)findViewById(R.id.btn_moist_1);
        btnmoist1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(last_stage=="LOW")
                {
                    int rand= ThreadLocalRandom.current().nextInt(682,1023);
                    status1.setText(rand+"");

                }
                else if(last_stage=="MEDIUM")
                {
                    int rand= ThreadLocalRandom.current().nextInt(341,682);
                    status1.setText(rand+"");


                }
                else
                {
                    int rand= ThreadLocalRandom.current().nextInt(0,341);
                    status1.setText(rand+"");

                }



            }});
        status=(TextView)findViewById(R.

















                id.status1);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not supported on this hardware platform",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        //textInfo.setText(stInfo);
        if(myThreadConnected!=null){
            byte[] bytesToSend = "3".getBytes();
            myThreadConnected.write(bytesToSend);
            byte[] NewLine = "\n".getBytes();
            myThreadConnected.write(NewLine);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device);
            }

            pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(this,
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    BluetoothDevice device =
                            (BluetoothDevice) parent.getItemAtPosition(position);
                    Toast.makeText(MoistureSensor.this,
                            "Name: " + device.getName() + "\n"
                                    + "Address: " + device.getAddress() + "\n"
                                    + "BondState: " + device.getBondState() + "\n"
                                    + "BluetoothClass: " + device.getBluetoothClass() + "\n"
                                    + "Class: " + device.getClass(),
                            Toast.LENGTH_LONG).show();

                    //          textStatus.setText("start ThreadConnectBTdevice");
                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
                    myThreadConnectBTdevice.start();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myThreadConnectBTdevice!=null){
            myThreadConnectBTdevice.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                setup();
            }

        }
        else if(requestCode==RESULT_SPEECH)
        {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                status.setText(text.get(0));
                if(text.get(0).equals("LED on")||text.get(0).equals("light on"))
                {
                    if(myThreadConnected!=null){
                        byte[] bytesToSend = "1".getBytes();
                        myThreadConnected.write(bytesToSend);
                        byte[] NewLine = "\n".getBytes();
                        myThreadConnected.write(NewLine);
                    }
                }
                else if(text.get(0).equals("LED of")||text.get(0).equals("light off"))
                {
                    if(myThreadConnected!=null){
                        byte[] bytesToSend = "2".getBytes();
                        myThreadConnected.write(bytesToSend);
                        byte[] NewLine = "\n".getBytes();
                        myThreadConnected.write(NewLine);
                    }
                }
                // Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(this,
                        "BlueTooth NOT enabled",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    /*
    ThreadConnectBTdevice:
    Background Thread to handle BlueTooth connecting
    */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                //    textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //          textStatus.setText("something wrong bluetoothSocket.connect(): \n" + eMessage);
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        textStatus.setText("");
                        //                      textByteCnt.setText("");
                        Toast.makeText(MoistureSensor.this, msgconnected, Toast.LENGTH_LONG).show();

                        listViewPairedDevice.setVisibility(View.GONE);
                        //inputPane.setVisibility(View.VISIBLE);
                    }
                });

                startThreadConnected(bluetoothSocket);

            }else{
                //fail
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[3027];
            int bytes;
            int clear=0;
            int count=0;
            char[] data;
            String actualmsg = "";

            String strRx = "";

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);


                    final String strReceived = new String(buffer, 0, bytes);
                    final String strByteCnt = String.valueOf(bytes) + " bytes received.\n";
                    Log.d("Byte received",strByteCnt);
                    Log.d("Data received",strReceived);
      //              actualmsg+=strReceived;
        //            count++;
                 if(strReceived.equals("h"))
                     actualmsg="HIGH";
                    else  if(strReceived.equals("m"))
                     actualmsg="MEDIUM";
                    else
                        actualmsg="LOW";

                    Log.d("actual Data received",actualmsg);
                    final String finalActualmsg = actualmsg;
    //                if(count==4) {
  //                      count = 0;
//                        actualmsg="";
                    last_stage =actualmsg;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                status.setText(" Moisture value = " + finalActualmsg);


                                //                        textStatus.append(strReceived);
                                //                      textByteCnt.append(strByteCnt);
                            }
                        });


                    //}



                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
//                            textStatus.setText(msgConnectionLost);
                        }});
                }



            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
