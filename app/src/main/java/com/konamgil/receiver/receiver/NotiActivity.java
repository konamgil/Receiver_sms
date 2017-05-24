package com.konamgil.receiver.receiver;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by konamgil on 2017-05-24.
 */

public class NotiActivity extends AppCompatActivity {

    static final int MY_PERMISSION_REQUEST_SMSSEND = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }
    /**
     * 퍼미션 체크 {READ_SMS, RECEIVE_SMS}
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "SEND_SMS", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, MY_PERMISSION_REQUEST_SMSSEND);
        } else {
            Toast.makeText(this, "허용", Toast.LENGTH_SHORT).show();
        }
    }
}
