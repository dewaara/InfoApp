package org.cdac.mdmclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.cdac.mdmclient.ModelResponse.LoginResponse;
import org.cdac.mdmclient.ModelResponse.RegisterResponse;
import org.cdac.mdmclient.ModelResponse.ShowDialogClass;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edt_login_username, edt_login_password, etServerIpAddress;
    Button btnLogin,btnConnect,btnRegister;

    TextView deviceId;
    TextView buildNo;
    TextView macAddress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin","false").equals("yes")){
            openDesh();
        }

        edt_login_username = (MaterialEditText) findViewById(R.id.edt_login_username);
        edt_login_password = (MaterialEditText) findViewById(R.id.edt_login_password);
        btnLogin = (Button) findViewById(R.id.bt_login);
        btnRegister = (Button) findViewById(R.id.bt_register);
        deviceId = (TextView) findViewById(R.id.deviceid);
        buildNo = (TextView) findViewById(R.id.buildNo);
        macAddress = (TextView) findViewById(R.id.macAddress);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        etServerIpAddress = (MaterialEditText) findViewById(R.id.edt_ipAddress);


        String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        deviceId.setText(androidId);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serverIp = etServerIpAddress.getText().toString();

                String USER_URL = ("http://" + serverIp + ":5001/device/RemoteLock/");

                if (TextUtils.isEmpty(serverIp)){
                    etServerIpAddress.setError("required Ip");
                }
                else if(USER_URL.equals(RetrofitClient.getInstance().BASE_URL)) {
                    Log.d("checkIPA","ip is.." + serverIp);
                    Toast.makeText(MainActivity.this, "match", Toast.LENGTH_SHORT).show();

                    btnRegister.setEnabled(true);
                    edt_login_username.setEnabled(true);
                    edt_login_password.setEnabled(true);
                    btnLogin.setEnabled(true); /////////////////////////////////////////


                }
                else {
                    Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_login_username.getText().toString();
                String password = edt_login_password.getText().toString();

                registerUser(username,
                        password,
                        deviceId.getText().toString());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = edt_login_username.getText().toString().trim();
                String password = edt_login_password.getText().toString().trim();

                if (username.isEmpty()){
                    edt_login_username.setError("Username required");
                    edt_login_username.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    edt_login_password.setError("Password required");
                    edt_login_password.requestFocus();
                    return;
                }

                Call<LoginResponse> call = RetrofitClient
                        .getInstance().getApi().Login(username,password);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Login Successful" +response.message(), Toast.LENGTH_SHORT).show();
                            Log.d("ookk",response.message());

                            editor.putString("isLogin","yes");
                            editor.commit();
                            openDesh();

                        }
                        else {
                            Log.d("oogf",response.message());
                            Toast.makeText(MainActivity.this, "error"+response.message(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void openDesh() {

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void registerUser(String username, String password, String deviceId) {

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi().register(username,password,deviceId);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
              //  RegisterResponse registerResponse=response.body();
                if (response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Register successful" +response.message(), Toast.LENGTH_SHORT).show();
                    Log.d("ok",response.message());

                    edt_login_username.setEnabled(true);
                    edt_login_password.setEnabled(true);
                    btnLogin.setEnabled(true);
                    btnRegister.setEnabled(false);

                }
                else {
                    Toast.makeText(getApplicationContext(), "else" +response.message(), Toast.LENGTH_SHORT).show();
                    Log.d("else",response.message());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "fail" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fail",t.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isDeviceScreenLocked()) {
            ShowDialogClass.showDialog(this, "Your Device is not secure. Please set pin or password for your screen lock");
        }
    }

    public boolean isDeviceScreenLocked() {
        return isDeviceLocked();
    }

    /**
     * @return true if pattern set, false if not (or if an issue when checking)
     */
    private boolean isPatternSet() {
        ContentResolver cr = this.getContentResolver();
        try {
            int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
            return lockPatternEnable == 1;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    /**
     * @return true if pass or pin set
     */
    private boolean isPassOrPinSet() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * @return true if pass or pin or pattern locks screen
     */
    private boolean isDeviceLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE); //api 23+
        return keyguardManager.isDeviceSecure();
    }
}