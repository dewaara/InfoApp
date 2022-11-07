package org.cdac.mdmclient;

import static android.content.ContentValues.TAG;
import static android.content.Context.DEVICE_POLICY_SERVICE;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.cdac.mdmclient.ModelResponse.ServerData;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SocketListener extends WebSocketListener {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName compName;

    Context context;

    SocketListener(Context context){
        this.context = context;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        Log.d(TAG, "onOpen: Connected: "+ webSocket.request());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
//        Log.d(TAG, "onMessage: "+text);
//        Log.d(TAG, "Lock method going to invoke ");

        compName = new ComponentName(context, MyAdmin.class);

        devicePolicyManager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);

        Type type = new TypeToken<ServerData>(){}.getType();
        Gson gson = new Gson();
        ServerData json = gson.fromJson(text, type);
        String deviceId = json.getDeviceId();
        String message = json.getMsg();
        Log.d(TAG, "onMessage: "+deviceId);

        if (deviceId.matches(Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID))){
            Log.d(TAG, "onMessage: device id matched");
            if (message.matches("lock")){
                Log.d(TAG, "Enter into Lock condition ");
                Log.d(TAG, "onMessage: "+json.getId());
                /*
                boolean active = devicePolicyManager.isAdminActive(compName);
                //Log.d("active",response.message());
                if (active){
                    devicePolicyManager.lockNow();
                   // Log.d("lockk",response.message());
                }
                else {
                    Toast.makeText(context, "You need to enable Admin device", Toast.LENGTH_SHORT).show();
                    //Log.d("pass",response.message());
                }
                */


            }else{
                Log.d(TAG, "Enter into Lock else condition ");
            }
        }

        /*else if (text =="wipe"){

        }*/
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG, "onClosed: "+reason);
    }
}
