package com.example.quiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class IncomingCall_Receiver extends BroadcastReceiver {

    String phoneNumber;
    SmsManager smsManager;
    String msg;

    @Override
    public void onReceive(Context context, Intent intent) {
        msg = "I'm busy, call you later";
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (phoneNumber != null) {
                    Toast.makeText(context, "Incoming call from: " + phoneNumber + "\nSent following message: " + msg, Toast.LENGTH_LONG).show();
                    smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
                }
            }
        }
    }
}