package com.example.thlab5_2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    private PowerStateChangeReceiver powerStateChangeReceiver;

    private void initValue() {
        powerStateChangeReceiver = new PowerStateChangeReceiver();
    }


    // XỬ LÝ KHI TIN NHẮN ĐẾN
    public void processReceive(Context context, Intent intent){

        Toast.makeText(context, getString(R.string.you_have_a_new_message), Toast.LENGTH_LONG).show();
        TextView tvContent = (TextView) findViewById(R.id.tv_content);

        final String SMS_EXTRA =  "pdus";
        Bundle bundle = intent.getExtras();

        Object[] messages = (Object[]) bundle.get(SMS_EXTRA);
        String sms = "";

        SmsMessage smsMsg;
        System.out.println("999999999999" + messages);
        for (int i=0; i < messages.length; i++) {
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                String format = bundle.getString("format");
                smsMsg = SmsMessage.createFromPdu((byte[]) messages[i], format);
                System.out.println(smsMsg);
            }
            else{

                smsMsg = SmsMessage.createFromPdu((byte[]) messages[i]);
            }
            // Get message body
            String msgBody = smsMsg.getMessageBody();

            // Get source address of message
            String address = smsMsg.getDisplayOriginatingAddress();
            sms += address +  ":\n" + msgBody + "\n";

        }
        //Show messages in textview
        tvContent.setText(sms);

        initValue();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        registerReceiver(
                powerStateChangeReceiver,
                intentFilter
        );
    }
    private void initBroadcastReceiver() {

        // Create filter to list:
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        // Create broadcastReceiver
        broadcastReceiver = new BroadcastReceiver() {
            // Process when message comes
            public void onReceive(Context context, Intent intent) {
                processReceive(context, intent);
            }
        };
    }
    @Override
    protected void onResume() {

        super.onResume () ;
        // Make sure broadcastReceiver vas created
        if (broadcastReceiver == null) initBroadcastReceiver();
        // Register Receiver
        registerReceiver (broadcastReceiver, filter);
    }

    @Override

    protected void onStop() {

        super.onStop();
        // UnregisterReceiver when app is destroyed
        unregisterReceiver(broadcastReceiver);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerStateChangeReceiver);
    }
}