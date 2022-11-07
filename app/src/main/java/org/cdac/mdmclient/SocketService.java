package org.cdac.mdmclient;

import static android.content.ContentValues.TAG;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.cdac.mdmclient.ModelResponse.ShowDialogClass;
import org.cdac.mdmclient.ModelResponse.SocketHandler;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class SocketService extends Service {

    private BroadcastReceiver broadcastReceiver;
    Context context;
    public SocketService() {
    }
    NetworkRequest networkRequest;
    private ConnectivityManager.NetworkCallback networkCallback;


    WebSocket webSocket;
    OkHttpClient client;
    Request request;
    Socket mSocket;
    private String counter;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

    String androidId;

    private String URL = "http://10.244.0.214:5001";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        SocketHandler.setSocket();
        mSocket = new SocketHandler().getSocket();
        mSocket.connect();
        mSocket.emit("join", androidId);
        mSocket.on("lock", onNewMessageLock);
        mSocket.on("wipe", onNewMessageWipe);


        //mSocket.emit("chat message", counter);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        String CHANNEL_ID = "1905";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        String CHANNEL_NAME = "MDM Channel";

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntentLaunchHomeScreen = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        }
        managerCompat.createNotificationChannel(notificationChannel);
        notificationBuilder.setSmallIcon(R.drawable.mdm)
                .setOngoing(true)
                .setContentTitle("MKavach 2")
                .setContentIntent(contentIntentLaunchHomeScreen)
                .setContentText("MDS Client is running...");
        Notification notification = notificationBuilder.build();

        int NOTIFICATION_ID = 215;
        startForeground(NOTIFICATION_ID, notification);

    }
    private final Emitter.Listener onNewMessageLock = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(args[0]!=null) {
                //Log.d(TAG, "call: "+args[0]);`
                String counter = (String) args[0];
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+counter);
                        if (counter.matches(androidId)){
                            if (devicePolicyManager.isAdminActive(compName)){
                                devicePolicyManager.lockNow();
                                Log.d(TAG, "Openxrun: "+mSocket.id()+ " "+androidId);
                                mSocket.emit("locked", true);
//                                mSocket.disconnect();
                            }else{
                                ShowDialogClass.showDialog(context, "Device Admin is disabled. Please enable it.");
                            }
                        }
                    }
                });
            }
        }
    };
    private final Emitter.Listener onNewMessageWipe = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(args[0]!=null) {
                //Log.d(TAG, "call: "+args[0]);
                String counter = (String) args[0];
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+counter);
                        if (counter.matches(androidId)){
                            if (devicePolicyManager.isAdminActive(compName)){
                                devicePolicyManager.lockNow();
                                Log.d(TAG, "run: "+mSocket.id());
                                mSocket.emit("wiped", true);
                            }else{
                                ShowDialogClass.showDialog(context, "Device Admin is disabled. Please enable it.");
                            }
                        }
                    }
                });
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Intent in = new Intent();
        in.setAction("YouWillNeverKillMe");
        sendBroadcast(in);
        Log.d(TAG, "onDestroy()...");
    }
}