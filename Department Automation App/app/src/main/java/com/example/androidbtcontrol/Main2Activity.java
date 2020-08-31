package com.example.androidbtcontrol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {
    Button  btn1;
    Button btn2;
    Bitmap photo;
    Button btn3;
    static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};

    private static DataOutputStream dataOutputStream;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn1 = (Button)findViewById(R.id.button);
     //   btn2 = (Button)findViewById(R.id.button2);
        btn3 = (Button)findViewById(R.id.button3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);

            }
        });
      /*  btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });*/
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moisture = new Intent(Main2Activity.this,MoistureSensor.class);
                startActivity(moisture);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && resultCode != RESULT_CANCELED && data!=null) {

             photo = (Bitmap) data.getExtras().get("data");
            new Thread(new ClientThread()).start();

            /*Socket sock;
            try {
                //sock = new Socket("MY_PCs_IP", 1149);
                sock = new Socket("192.168.162.5", 1149);
                System.out.println("Connecting...");

                // sendfile

                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File myFile = new File (getRealPathFromURI(tempUri));
                byte [] mybytearray  = new byte [(int)myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                OutputStream os;
                BufferedReader in;
                String userInput;
                try (BufferedInputStream bis = new BufferedInputStream(fis)) {
                    bis.read(mybytearray, 0, mybytearray.length);
                }
                os = sock.getOutputStream();
                System.out.println("Sending...");
                os.write(mybytearray,0,mybytearray.length);
                os.flush();

                //RESPONSE FROM THE SERVER
                //in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                //in.ready();
                //userInput = in.readLine();
                //System.out.println("Response from server..." + userInput);

                sock.close();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

*/
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    class ClientThread implements Runnable {
        Socket socket;
     //   String ip="192.168.43.210";
     String ip="192.168.162.3";
        //public static String Time;
        //Socket_Client sc=new Socket_Client();
        public void run() {


            try {
                //   textView.setText("In Thread");
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, 1149);

                Uri tempUri = getImageUri(getApplicationContext(), photo);

                File myFile = new File (getRealPathFromURI(tempUri));
           /*     byte [] mybytearray  = new byte [(int)myFile.length()];
                Log.d("mymessage",myFile.length()+"");
                FileInputStream fis = new FileInputStream(myFile);

                // Toast.makeText(Main2Activity.this,"Connected",Toast.LENGTH_LONG).show();

              //  PrintWriter printwriter =  new PrintWriter(socket.getOutputStream(),true);


                //printwriter.println("sink");
                //    Toast.makeText(Main2Activity.this,"Request Send",Toast.LENGTH_LONG).show();
                //     textView.setText("Request Send");
               // printwriter.flush();
//                  Toast.makeText(Main2Activity.this,"Reqeust Send",Toast.LENGTH_LONG).show();
                OutputStream os;
                BufferedReader in;
                String userInput;
                try (BufferedInputStream bis = new BufferedInputStream(fis)) {
                    bis.read(mybytearray, 0, mybytearray.length);
                }
                os = socket.getOutputStream();
                System.out.println("Sending...");
                os.write(mybytearray,0,mybytearray.length);
                os.flush();

              //new
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File myFile = new File(getRealPathFromURI(tempUri));
                System.out.println("Sending...");
                FileInputStream imageInFile = new FileInputStream(myFile);
                byte imageData[] = new byte[(int) myFile.length()];
                imageInFile.read(imageData);
                String imageDataString = encodeImage(imageData);
                System.out.println(imageDataString.length());

                System.out.println(imageDataString);
                dataOutputStream.writeUTF(imageDataString);

                dataOutputStream.flush();*/
                //end new



                // latest new
              /*  byte[] bytes = new byte[(int) file.length()];
                BufferedInputStream bis;

                    bis = new BufferedInputStream(new FileInputStream(file));
                    bis.read(bytes, 0, bytes.length);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(bytes);
                    oos.flush();*/
                //end new

                System.out.println((int)myFile.length());
                byte[] mybytearray  = new byte[450560];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                OutputStream os = socket.getOutputStream();
                System.out.println("Sending...");
                os.write(mybytearray,0,mybytearray.length);
                os.flush();
                    socket.close();
                Main2Activity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this,"Successfully connected...Attendence is being processed..",Toast.LENGTH_LONG).show();

                    }
                });





            } catch (UnknownHostException e1) {
                final String error =e1.toString();
//                Toast.makeText(Main2Activity.this,"Exception "+error,Toast.LENGTH_LONG).show();
                Main2Activity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this,"Connection problem + error",Toast.LENGTH_LONG).show();

                    }
                });
                //e1.printStackTrace();
                //   textView.setText(e1.toString());

            } catch (IOException e1) {
                final String error =e1.toString();
                //              Toast.makeText(Main2Activity.this,"Exception "+e1.toString(),Toast.LENGTH_LONG).show();
                Main2Activity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this,"Connection problem"+ error,Toast.LENGTH_LONG).show();

                    }
                });
                // textView.setText(e1.toString());
                //e1.printStackTrace();

            }
            catch(Exception e)
            {
                final String error =e.toString();
                Main2Activity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(Main2Activity.this,"Connection problem "+ error,Toast.LENGTH_LONG).show();

                    }
                });
            }



        }

    }
    public static String encodeImage(byte[] imageByteArray) {
       return Base64.encodeBase64String(imageByteArray);
//        return StringUtils.newStringUtf8(encodeBase64(imageByteArray, false));

    }
   /* public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
        if(binaryData != null && binaryData.length != 0) {
            Base64 b64 = isChunked?new Base64(urlSafe):new Base64(0, CHUNK_SEPARATOR, urlSafe);
            long len = b64.getEncodedLength(binaryData);
            if(len > (long)maxResultSize) {
                throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
            } else {
                return b64.encode(binaryData);
            }
        } else {
            return binaryData;
        }
    }
    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
        return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
    }
    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        return encodeBase64(binaryData, isChunked, false);
    }*/
}
