package com.example.cdac.expsocketapp;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import org.cdac.myutilslib.utils.userInteraction.L;

public class AdminReceiver extends DeviceAdminReceiver {

    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        L.d("Sample Device Admin: enabled");
    }


    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This is a message to warn the user about disabling.";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        L.d("Device Admin : disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        L.d("Sample Device Admin: pw changed");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        L.d("Sample Device Admin: pw failed");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        L.d("Sample Device Admin: pw succeeded");
    }
}
