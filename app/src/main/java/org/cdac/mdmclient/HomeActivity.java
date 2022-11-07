package org.cdac.mdmclient;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.cdac.mdmclient.ModelResponse.DeviceLock;
import org.cdac.mdmclient.ModelResponse.SocketHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button disable, enable;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

    NetworkRequest networkRequest;
    private ConnectivityManager.NetworkCallback networkCallback;



//    private Socket mSocket;
//
//    {
//        try {
//            mSocket = IO.socket("http://10.244.0.214:5001/device/RemoteLock");
//        } catch (URISyntaxException e) {}
//    }


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String androidId;
    Socket mSocket;
    private String counter;
    private TextView lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        lock = (TextView) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.enablebtn);
        disable = (Button) findViewById(R.id.disableBtn);
        //lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        SocketHandler.setSocket();
        mSocket = new SocketHandler().getSocket();
        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);

                mSocket.connect();
                mSocket.emit("join",androidId);
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                mSocket.disconnect();
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            }
        };
        ConnectivityManager connectivityManager =
                getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);

//        if (!sharedPreferences.getString("isLogin","false").equals("yes")){
//           finish();
//        }
        if (!devicePolicyManager.isAdminActive(compName)){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent, RESULT_ENABLE);
        }

//        if (sharedPreferences.getString("isLogin","false").equals("false")){
//            openLogin();
//        }
//        mSocket.on("chat messsage", onNewMessage);
//        mSocket.connect();




    }

    private void openLogin() {
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changePassword:
                Toast.makeText(this, "Change password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                editor.putString("isLogin","false");
                editor.commit();
                openLogin();
                break;
        }
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {


     //   DeviceLock isDeviceLock = new DeviceLock();

      //  boolean isDeviceLock = true;




        startService(new Intent(getApplicationContext(),  SocketService.class));
//        SocketHandler.setSocket();
//        SocketHandler.establishConnection();
//
//        Call<DeviceLock> call = RetrofitClient
//                .getInstance()
//                .getApi().RemoteLock(true);
//        call.enqueue(new Callback<DeviceLock>() {
//            @Override
//               public void onResponse(Call<DeviceLock> call, Response<DeviceLock> response) {
//                if (response.isSuccessful())
//                {
//                  /*  Log.d("haa",response.message());
//                    if (view == lock){
//                        boolean active = devicePolicyManager.isAdminActive(compName);
//                        Log.d("active",response.message());
//                        if (active){
//                            devicePolicyManager.lockNow();
//                            Log.d("lockk",response.message());
//                        }
//                        else {
//                            Toast.makeText(HomeActivity.this, "You need to enable Admin device", Toast.LENGTH_SHORT).show();
//                            Log.d("pass",response.message());
//                        }
//                    } else if (view == enable){
//
//                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
//                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
//                        startActivityForResult(intent, RESULT_ENABLE);
//                        Log.d("ena",response.message());
//
//                    } else if (view == disable){
//                        Log.d("dis",response.message());
//                        devicePolicyManager.removeActiveAdmin(compName);
//                        disable.setVisibility(View.GONE);
//                        enable.setVisibility(View.VISIBLE);
//                    }*/
//                } else {
//                    Toast.makeText(HomeActivity.this, "error" +response.message(), Toast.LENGTH_SHORT).show();
//                    Log.d("woh",response.message());
//                }
//            }

//            @Override
//            public void onFailure(Call<DeviceLock> call, Throwable t) {
//                Toast.makeText(HomeActivity.this, "fail: " +t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "You have enabled the Admin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Problem to enable Admin Device", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        Log.d(TAG, "run: ");
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                  //  addMessage(username, message);
                }
            });
        }
    };
}