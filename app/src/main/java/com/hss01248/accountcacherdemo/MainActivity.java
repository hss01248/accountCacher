package com.hss01248.accountcacherdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hss01248.accountcache.AccountCacher;
import com.hss01248.accountcache.AccountCallback;
import com.hss01248.accountcache.DebugAccount;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void select(View view) {
        AccountCacher.selectAccount(AccountCacher.TYPE_TEST, this, "in", new AccountCallback() {
            @Override
            public void onSuccess(DebugAccount account) {
                Toast.makeText(MainActivity.this,account.toString(),Toast.LENGTH_LONG).show();
                InputDialogUtil.showDialog(MainActivity.this, account, new InPutCallback() {
                    @Override
                    public void onGet(String account, String pw) {
                        AccountCacher.saveAccount(MainActivity.this,AccountCacher.TYPE_TEST,"in",account,pw);
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

    }

    public void save(View view) {
        AccountCacher.saveAccount(this,AccountCacher.TYPE_TEST,"in","xiaomihuawei","1234556x");
        AccountCacher.saveAccount(this,AccountCacher.TYPE_TEST,"in","bigfish","987654321y");
        AccountCacher.saveAccount(this,AccountCacher.TYPE_TEST,"in","19684579154","abcdefg");
    }
}