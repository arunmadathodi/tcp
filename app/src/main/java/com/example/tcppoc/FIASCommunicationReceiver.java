package com.example.tcppoc;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FIASCommunicationReceiver {

    private static final String TAG = "FIASCommunicationReceiver";
    private static final String SERVER_IP = "your_server_ip";
    private static final int SERVER_PORT = 1234; // Replace with the actual port
    private static final int TIMEOUT_THRESHOLD = 3; // Number of consecutive timeouts before considering link inactive

    private int consecutiveTimeouts = 0;
    private CommunicationListener communicationListener;
    private ExecutorService executorService;

    public FIASCommunicationReceiver(CommunicationListener listener) {
        this.communicationListener = listener;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        executorService.execute(new CommunicationRunnable());
    }

    public void stop() {
        executorService.shutdownNow();
    }



    private class CommunicationRunnable implements Runnable {
        @Override
        public void run() {
            try {
                // Step 1: Establish a TCP/IP connection
                Socket socket = new Socket("192.168.18.108", 5678);

                // Step 2: Handle the initial Link Start (LS) record
                handleInitialLinkStart(socket);



            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
    }

    private void handleInitialLinkStart(Socket socket) throws IOException {
        // Listen for a message from the PMS for at least 3 seconds
        socket.setSoTimeout(10000);

        try {
            // Example: Receive Link Start (LS) Record
            String lsRecord = receiveMessage(socket);
            Log.d(TAG, "Received LS Record: " + lsRecord);

            // If received a Link Start (LS), respond with Link Description (LD) and Link Records/LA sequence
            if (lsRecord.startsWith("LS")) {
                sendLinkDescription(socket);
                sendLinkRecords(socket);
                sendLinkAlive(socket);
            }
        } catch (IOException e) {
            // No message received within the timeout
            Log.d(TAG, "No message received within the timeout");
            sendLinkStart(socket);
        }
    }

    private void processMessages(Socket socket) throws IOException {
        while (!Thread.interrupted()) {
            // Example: Receive messages continuously
            String message = receiveMessage(socket);
            Log.d(TAG, "Received Message: " + message);

            // Publish the received message to the UI thread
            communicationListener.onMessageReceived(message);

            // Implement logic to process different types of messages

            // For simplicity, assume consecutive timeouts indicate link inactivity
            if (message == null) {
                consecutiveTimeouts++;
                if (consecutiveTimeouts >= TIMEOUT_THRESHOLD) {
                    handleInactiveLink(socket);
                    break;
                }
            } else {
                // Reset consecutive timeouts counter
                consecutiveTimeouts = 0;
            }
        }
    }

    private void sendLinkStart(Socket socket) throws IOException {
        // Example: Send Link Start (LS) Record
        sendMessage(socket, "LS|DA170831|TI102201|");
    }

    private void sendLinkDescription(Socket socket) throws IOException {
        // Example: Send Link Description (LD) Record
        sendMessage(socket, "LD|DA170831|TI102201|IFPB|V#1.13|RT4|CGFidCryptAB;3k57hsoHm04fGEyaA3+UVw==oNTebC0J36LY8GV9azyhzw==|");
    }

    private void sendLinkRecords(Socket socket) throws IOException {
        // Example: Send multiple Link Records (LR)
        String lrRecord1 = "LR|RIRE|FLRNDNMLCSVMRTRSID|";
        String lrRecord2 = "LR|RIPS|FLRNRTTADUDDPTM#MAIDX1SOPXMPDATI|";
        // Add more Link Records as needed

        // Encrypt IfcAuthKey and include it in the CG field of each LR record
        String encryptedAuthKey = encryptIfcAuthKey("GVDpVnl6qYlTQXQJZxXdbw==");
        lrRecord1 += "|CG" + encryptedAuthKey + "|";
        lrRecord2 += "|CG" + encryptedAuthKey + "|";

        // Send Link Records
        sendMessage(socket, lrRecord1);
        sendMessage(socket, lrRecord2);
        // Add more Link Records as needed
    }

    private void sendLinkAlive(Socket socket) throws IOException {
        // Example: Send Link Alive (LA) Record
        sendMessage(socket, "LA|DA170831|TI102201|");
    }

    private void handleInactiveLink(Socket socket) throws IOException {
        Log.d(TAG, "Link is inactive. Closing and waiting for reconnection.");

        // Example: Send Link End (LE) Record
        sendMessage(socket, "LE|DA170831|TI102201|");

        // Close the connection and wait for reconnection
        socket.close();

        // Implement logic to wait for reconnection and handle database synchronization
        // ...

        // Re-establish the connection and restart the link initialization sequence
        handleInitialLinkStart(socket);
    }

    // Remaining code for sendMessage, receiveMessage, encryptIfcAuthKey, etc.

    public void sendMessage(Socket socket, String message) throws IOException {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(message);
        writer.flush();
    }

    private String receiveMessage(Socket socket) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }

    private String encryptIfcAuthKey(String authKey) {
        // Implement the logic to encrypt the IfcAuthKey
        // ...

        // For example, return the original key for demonstration purposes
        return authKey;
    }

    public interface CommunicationListener {
        void onMessageReceived(String message);
    }
}
