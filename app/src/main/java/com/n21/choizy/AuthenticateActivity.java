package com.n21.choizy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.n21.choizy.adapter.network_change_listner;

public class AuthenticateActivity extends AppCompatActivity {

    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        myLocal.getPersistedData(this,"en");
    }
}