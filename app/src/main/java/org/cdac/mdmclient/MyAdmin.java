package org.cdac.mdmclient;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.DEVICE_POLICY_SERVICE;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyAdmin extends DeviceAdminReceiver {
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context, "Device Admin : enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context, "Device Admin : disible", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public CharSequence onDisableRequested(@NonNull Context context, @NonNull Intent intent) {
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);

        return onDisableRequested(context, intent);
    }
}
