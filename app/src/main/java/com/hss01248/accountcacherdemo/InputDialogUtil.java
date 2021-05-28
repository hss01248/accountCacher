package com.hss01248.accountcacherdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.hss01248.accountcache.DebugAccount;

public class InputDialogUtil {

    public static void showDialog(Activity activity, DebugAccount account,InPutCallback callback){
        View view  = View.inflate(activity,R.layout.dialog_input,null);
        EditText etAccount = view.findViewById(R.id.et_account);
        EditText tepw = view.findViewById(R.id.et_pw);

        if(account != null){
            etAccount.setText(account.account);
            tepw.setText(account.pw);
        }

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onGet(etAccount.getText().toString().trim(),tepw.getText().toString().trim());

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }
}
