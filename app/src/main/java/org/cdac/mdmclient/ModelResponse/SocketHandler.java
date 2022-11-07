package org.cdac.mdmclient.ModelResponse;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

public class SocketHandler {

    private static final String TAG = SocketHandler.class.getSimpleName();
    private static Socket mSocket;
    public static void setSocket(){
        try {
//            IO.Options options = new IO.Options();
//            options.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket("http://10.244.0.214:5001/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return mSocket;
    }

    public static void establishConnection(){
        mSocket.connect();
        Log.d(TAG, "establishConnection: "+mSocket.connected());
    }

    public void closeConnection(){
        mSocket.disconnect();
    }
}
