package com.wsp.thinkpad.bb;

/**
 * Created by ${吴心良}
 * on 2017/4/11.
 * description:
 */

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver{
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public SmsReceiver() {
        Log.e("sms", "new SmsReceiver");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        Log.e("sms", "接收");
        Cursor cursor = null;
        try {
            if (SMS_RECEIVED.equals(intent.getAction())) {
                Log.e("sms", "sms received!");
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > 0) {
                        String msgBody = messages[0].getMessageBody();
                        String msgAddress = messages[0].getOriginatingAddress();
                        long msgDate = messages[0].getTimestampMillis();
                        String smsToast = "New SMS received from : "
                                + msgAddress + "\n'"
                                + msgBody + "'";
                        Toast.makeText(context, smsToast, Toast.LENGTH_LONG)
                                .show();
                        Log.e("sms", "message from: " + msgAddress + ", message body: " + msgBody
                                + ", message date: " + msgDate);
                        addSmsToDB(context,msgAddress,msgBody);
                    }
                }
                cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[] { "_id", "address", "read", "body", "date" }, "read = ? ", new String[] { "0" }, "date desc");
                if (null == cursor){
                    return;
                }

                Log.e("sms","m cursor count is "+cursor.getCount());
                Log.e("sms","m first is "+cursor.moveToFirst());

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sms", "Exception : " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }
    /**

     *添加到系统短信数据库

     */

    private void addSmsToDB(Context context,String address, String content) {
        ContentValues values = new ContentValues();
        values.put("date", System.currentTimeMillis());
        values.put("read", 0);//0为未读信息
        values.put("type", 1);//1为收件箱信息
        values.put("address", address);
        values.put("body", content);
        context.getContentResolver().insert(Uri.parse("content://sms"),
                values);
    }
}  