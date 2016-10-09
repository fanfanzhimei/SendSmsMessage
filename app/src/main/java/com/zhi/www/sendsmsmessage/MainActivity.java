package com.zhi.www.sendsmsmessage;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
    public static final String SMS_SEND_ACTION = "sms_send_action";
    public static final String SMS_DELIVERED_ACTION = "sms_delivered_action";

    private String phone, message;
    private EditText mEtPhone, mEtMessage;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvent();
    }

    private void initViews() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mBtnSend = (Button) findViewById(R.id.btn_send);
    }

    private void initEvent() {
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
        }
    }

    private void send() {
        checkData();

        Intent send = new Intent(SMS_SEND_ACTION);
        Intent deliver = new Intent(SMS_DELIVERED_ACTION);
        PendingIntent mSendPI = PendingIntent.getBroadcast(MainActivity.this, 0, send, 0);
        PendingIntent mDeliverPI = PendingIntent.getBroadcast(MainActivity.this, 0, deliver, 0);

        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> texts = manager.divideMessage(message);

        for (String text : texts) {
            manager.sendTextMessage(phone, null, text, mSendPI, mDeliverPI);
        }
        Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
    }

    private boolean checkData() {
        Editable ePhone = mEtPhone.getText();
        Editable eMessage = mEtMessage.getText();

        if (null == ePhone || null == eMessage) {
            return true;
        }

        phone = ePhone.toString();
        message = eMessage.toString();

        if ("".equals(phone.trim())) {
            Toast.makeText(MainActivity.this, "信息接收人不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ("".equals(message)) {
            Toast.makeText(MainActivity.this, "信息不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

class MyServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MainActivity.SMS_SEND_ACTION)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(App.getContext(), "消息发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(App.getContext(), "消息发送失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if (intent.getAction().equals(MainActivity.SMS_DELIVERED_ACTION)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(App.getContext(), "成功接收消息", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(App.getContext(), "接收消息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}