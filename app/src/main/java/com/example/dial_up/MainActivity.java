/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dial_up;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String SERVER_PHONE_NUM = "YOUR_SERVER_PHONE_NUMBER";
    private static final int SMS_PERMISSION_CODE = 1234;
    private SmsReceiver smsBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsBroadcastReceiver = new SmsReceiver(this.SERVER_PHONE_NUM);
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsBroadcastReceiver.setListener(new SmsReceiver.Listener(){
            @Override
            public void onTextReceived (Context context, Intent intent, String text){
                Intent i = new Intent(context, Website.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("WEBSITE_HTML", text);
                context.startActivity(i);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        return;
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        String[] permissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS
        };
        ActivityCompat.requestPermissions(this, permissions, SMS_PERMISSION_CODE);
    }

    private boolean hasValidPreConditions() {
        if (!isSmsPermissionGranted()) {
            requestSmsPermission();
            return false;
        }
        return true;
    }


    public void smsSendMessage(View view) {
        if (!hasValidPreConditions()) return;

        String destinationAddress = this.SERVER_PHONE_NUM;

        EditText smsEditText = findViewById(R.id.url);
        String smsMessage = smsEditText.getText().toString();

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.url).setVisibility(View.GONE);
        findViewById(R.id.load).setVisibility(View.GONE);

        SmsManager.getDefault().sendTextMessage(destinationAddress, null, smsMessage, null, null);
        Toast.makeText(getApplicationContext(), "Requesting your website!", Toast.LENGTH_SHORT).show();
    }
}
