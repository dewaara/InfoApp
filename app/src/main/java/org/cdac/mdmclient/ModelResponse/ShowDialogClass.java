package org.cdac.mdmclient.ModelResponse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class ShowDialogClass {
    public static void showDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
