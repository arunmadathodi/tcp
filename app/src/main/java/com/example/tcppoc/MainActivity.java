package com.example.tcppoc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText editText;
    private EditText msgTxt;
    private TCPClientHandler clientHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        editText = findViewById(R.id.editText);
        msgTxt = findViewById(R.id.msg);

        try {
            Socket socket = new Socket("192.168.1.7", 5678);
            clientHandler = new TCPClientHandler(socket);
            new Thread(clientHandler).start();
        } catch (IOException e) {
            Log.e(TAG, "Error creating socket", e);
        }
    }



    public void setGuestData(String message){
        HotelGuestInfo guestInfo = new HotelGuestInfo();
        String data = message;
        String[] parts = data.split("\\|");
        HashMap<String, String> keyValueMap = new HashMap<>();
        for (String part : parts) {
            String key = part.substring(0, 2);
            String value = (part.length() > 2) ? part.substring(2) : "";
            keyValueMap.put(key, value);
        }
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            switch (entry.getKey()){
                case "GN":
                    guestInfo.setGuestName(entry.getValue());
                    break;
                case "RN":
                    guestInfo.setRoomNumber(entry.getValue());
                    break;
                case "GL":
                    guestInfo.setGuestLanguage(entry.getValue());
                    break;
                case "G#":
                    guestInfo.setReservationNumber(entry.getValue());
                    break;
                case "GS":
                    guestInfo.setGuestShare(entry.getValue());
                    break;
                case "TI":
                    guestInfo.setCheckInTime(entry.getValue());
                    guestInfo.setCheckoutTime(entry.getValue());
                    break;

                default:

                    break;

            }
        }
        System.out.println("Document ID: " + guestInfo.getId());
        System.out.println("Company: " + guestInfo.getCompany());
        System.out.println("Hotel ID: " + guestInfo.getHotelId());
        System.out.println("Guest Name: " + guestInfo.getGuestName());
        System.out.println("Channels: " + guestInfo.getChannels());
        System.out.println("Room Number: " + guestInfo.getRoomNumber());
        System.out.println("Guest Language: " + guestInfo.getGuestLanguage());
        System.out.println("Reservation Number: " + guestInfo.getReservationNumber());
        System.out.println("Guest Share: " + guestInfo.getGuestShare());
        System.out.println("Check-In Time: " + guestInfo.getCheckInTime());
        System.out.println("Check-Out Time: " + guestInfo.getCheckoutTime());
        System.out.println("Active: " + guestInfo.getActive());
        System.out.println("Metadata: " + guestInfo.getMetadata());
        System.out.println("Class Name: " + guestInfo.getClassName());

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (clientHandler != null) {
            clientHandler.sendMessage("exit");
            clientHandler.close();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void btnClick(View view) {
        String command = editText.getText().toString();
//        if (clientHandler != null) {
//            clientHandler.sendMessage(command);
//        }
        try {
            View v = this.getCurrentFocus();
            if (v == null) {
                v = new View(this).getRootView();
            }
            final IBinder windowToken = v.getWindowToken();
            InputMethodManager keyboard = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        } catch (Exception e) {
            Log.e(TAG, "Error hiding keyboard", e);
        }
    }
}
