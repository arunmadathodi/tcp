package com.example.tcppoc;


import android.util.Log;

import com.example.tcppoc.HotelGuestInfo;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TCPClientHandler implements Runnable {

    private static final String TAG = TCPClientHandler.class.getSimpleName();


    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    private boolean linkStarted = false;

    public TCPClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            // Open communication port and listen for 3 seconds
            Thread.sleep(3000);

            while (!Thread.currentThread().isInterrupted()) {
                // Check if there is data available to read
                if (dataInputStream.available() > 0) {
                    String receivedMsg = dataInputStream.readUTF();
                    if (receivedMsg != null && !receivedMsg.isEmpty()) {

                        if (receivedMsg.startsWith("LS")) {
                            handleLinkStart();
                        } else if (receivedMsg.startsWith("LE")) {
                            handleLinkEnd();
                        } else {
                            setGuestData(receivedMsg);
                        }
                    }
                }
                // Add a short delay to avoid busy-waiting and to give time for other threads
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Error reading message", e);
        }
    }

    private void handleLinkStart() {
        if (!linkStarted) {
            sendMessage("LD|"+getCurrentDate()+"|"+getCurrentTime()+"|V#11.987.256"+"|IFMS|");
            sendMessage("LR|RIGI|FLRNG#GSGNGLGVGGGAGDA0A1SF|");
            sendMessage("LR|RIGC|FLRNG#GSGNGLGVGGGAGDA0A1RO|");
            sendMessage("LR|RIGO|FLRNG#GSSF|");
            sendMessage("LR|RIPR|FLRNG#PTTAX1DATIP#|");
            sendMessage("LR|RIPA|FLRNASDATIP#G#GN|");
            linkStarted = true;
        } else {
            // Receiving LS after link initiation, respond with LS
            sendMessage("LS|");
        }
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Create formatted strings for date and time
        String formattedHour = String.valueOf(hour);
        String formattedMinute = String.valueOf(minute);
        String formattedSecond = String.valueOf(second);
        String formattedTime = "TI" + formattedHour + formattedMinute + formattedSecond;
        return formattedTime;

    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        // Get the current day, year, and month
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, so add 1
        String formattedDay = String.valueOf(dayOfMonth);
        String formattedYear = String.valueOf(year);
        String formattedMonth = String.valueOf(month);
        String date = "DA"+formattedYear+formattedMonth+formattedDay;
        return date;
    }

    private void handleLinkEnd() {
        // Handle Link End (LE) for normal shutdown
        close();
    }

    private void setGuestData(String message) {

        HotelGuestInfo guestInfo = parseGuestData(message);

        printGuestInfo(guestInfo);
    }


    private HotelGuestInfo parseGuestData(String message) {
        HotelGuestInfo guestInfo = new HotelGuestInfo();
        String[] parts = message.split("\\|");
        HashMap<String, String> keyValueMap = new HashMap<>();
        for (String part : parts) {
            String key = part.substring(0, 2);
            String value = (part.length() > 2) ? part.substring(2) : "";
            keyValueMap.put(key, value);
        }
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            switch (entry.getKey()) {
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
                case "A0":
                    guestInfo.setGuestEmailId(entry.getValue());
                    break;
                case "A1":
                    guestInfo.setContactNumber(entry.getValue());
                    break;
                case "TI":
                    setCheckInOutTime(message,entry.getValue(), guestInfo);
                    break;
                default:
                    break;
            }
        }
        return guestInfo;
    }

    private void setCheckInOutTime(String message, String time, HotelGuestInfo guestInfo) {
        // Check if the message starts with "GI" or "GO" and set TI accordingly
        if (time != null && !time.isEmpty()) {
            if (message.startsWith("GI")) {
                guestInfo.setCheckInTime(time);
            } else if (message.startsWith("GO")) {
                guestInfo.setCheckoutTime(time);
            }
        }
    }



    private void printGuestInfo(HotelGuestInfo guestInfo) {
        // Print guest information to the console (adjust as needed)
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


    public void sendMessage(String msg) {
        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();

        } catch (IOException e) {

        }
    }

    public boolean isSocketConnected() {
        return socket != null && socket.isConnected();
    }

    public void close() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing resources", e);
        }
    }
}
