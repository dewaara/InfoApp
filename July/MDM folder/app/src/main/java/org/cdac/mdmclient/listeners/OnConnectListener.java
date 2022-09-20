package com.example.cdac.expsocketapp.listeners;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.example.cdac.expsocketapp.AppConfig;
import com.example.cdac.expsocketapp.MySocket;
import com.example.cdac.expsocketapp.dtos.AppDataDto;
import com.example.cdac.expsocketapp.dtos.AuthDto;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.cdac.myutilslib.utils.Storage.SharedPrefs.SharedPref;

import java.util.List;

public class OnConnectListener implements Emitter.Listener {

    private Context context;

    public OnConnectListener(Context context) {
        this.context = context;
    }

    @Override
    public void call(Object... args) {
        sendInstalledAppData(context);

        TelephonyManager tManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        String imei = tManager.getDeviceId();

        AuthDto authCommand = new AuthDto(SharedPref.getString(context, AppConfig.SPF_TOKEN, ""),imei);


        MySocket.getInstance(context).getSocket().emit("authenticate", new Gson().toJson(authCommand));
    }


    private void sendInstalledAppData(Context context) {
        List<PackageInfo> packList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppDataDto appData = new AppDataDto();

                appData.setName(SharedPref.getString(context, AppConfig.SPF_TOKEN, ""));
                appData.setPkg(packInfo.packageName);
                appData.setVersion(packInfo.versionName);


                MySocket.getInstance(context).getSocket().emit("updateverify", new Gson().toJson(appData));
            }
        }
    }
}
