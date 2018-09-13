package com.wsp.thinkpad.bb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
    SmsReceiver myReceiver;
    Button btn;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("sms","onCreate");
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setDefaultSms();
            }
        });
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //sendSms();
                sendSMS("10086","10086");
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteSms();
            }
        });
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                test();
            }
        });
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteSendSms();
            }
        });
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSms(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("click_getsms", "getsms");
            }
        });
        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteSendDialogSms();
            }
        });
        btn7 = (Button) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_SMS}, 100);
                }
            }
        });
    }

    private void deleteSendDialogSms() {
        Cursor cursor = null;
        ContentResolver resolver=getContentResolver();
        try {
            cursor = getContentResolver().query(Uri.parse("content://sms/sent"), new String[] { "_id", "address", "body","thread_id" }, null, null, "date desc");
            long id=-1;
            long threadId=-1;
            if (cursor != null&&cursor.getCount()>0&&cursor.moveToFirst()) {
                id=cursor.getLong(0);
                String address=cursor.getString(1);
                String body=cursor.getString(2);
                threadId = cursor.getLong(3);
                Log.e("sms","address:"+address+"bady:"+body+"threadId:"+threadId);
                Toast.makeText(MainActivity.this,String.format("address: %s\n body: %s",address,body),Toast.LENGTH_LONG).show();
                Log.e("sms","id:"+id);
            }
            if(threadId!=-1){
                int count=resolver.delete(Uri.parse("content://sms/conversations/" + threadId),null,null);
                Log.e("sms","count:"+count);
                Toast.makeText(MainActivity.this,count==5?"删除成功":"删除失败",Toast.LENGTH_LONG).show();
                Log.e("sms",count==5?"删除成功":"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sms",e.getLocalizedMessage()
            );
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }
    private void deleteSendSms() {
        Cursor cursor = null;
        ContentResolver resolver=getContentResolver();
        try {
            cursor = getContentResolver().query(Uri.parse("content://sms/sent"), new String[] { "_id", "address", "body","thread_id" }, null, null, "_id desc");
            long id=-1;
            long threadId=-1;
            if (cursor != null&&cursor.getCount()>0&&cursor.moveToFirst()) {
                id=cursor.getLong(0);
                String address=cursor.getString(1);
                String body=cursor.getString(2);
                threadId = cursor.getLong(3);
                Log.e("sms","address:"+address+"bady:"+body+"threadId:"+threadId);
                Toast.makeText(MainActivity.this,String.format("address: %s\n body: %s",address,body),Toast.LENGTH_LONG).show();
                Log.e("sms","id:"+id);
            }
            if(id!=-1){
                int count=resolver.delete(Telephony.Sms.CONTENT_URI,"_id="+id,null);
                Log.e("sms","count:"+count);
                Toast.makeText(MainActivity.this,count==1?"删除成功":"删除失败",Toast.LENGTH_LONG).show();
                Log.e("sms",count==1?"删除成功":"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sms",e.getLocalizedMessage()
            );
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }
    private void deleteSms() {
        Cursor cursor = null;
        ContentResolver resolver=getContentResolver();
        try {
            cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "body" }, null, null, "_id desc");
            long id=-1;
            if (cursor != null&&cursor.getCount()>0&&cursor.moveToFirst()) {
                id=cursor.getLong(0);
                String address=cursor.getString(1);
                String body=cursor.getString(2);
                Toast.makeText(MainActivity.this,String.format("address: %s\n body: %s",address,body),Toast.LENGTH_LONG).show();
            }
            if(id!=-1){
                int count=resolver.delete(Telephony.Sms.CONTENT_URI,"_id="+id,null);
                Toast.makeText(MainActivity.this,count==1?"删除成功":"删除失败",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }

    private void sendSms() {
        SmsManager sms = SmsManager.getDefault();
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
        sms.sendTextMessage("10086", null, "10086", pi, null);

    }

    /**
     * 直接调用短信接口发短信
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber,String message){

        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));


        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(MainActivity.this,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }


    private void setDefaultSms() {
        final String myPackageName = getPackageName();
        Log.e("sms",Telephony.Sms.getDefaultSmsPackage(this));
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);
            Log.e("sms", "11111111111");
        }

        if(!Telephony.Sms.getDefaultSmsPackage(this).equals("com.android.mms")){
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, "com.android.mms");
            startActivity(intent);
        }
    }
    //将新短信设置为已读
    public void test(){
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "read" }, "read = ? ", new String[] {"0" }, "date desc");
            if (cursor != null) {
                ContentValues values = new ContentValues();
                values.put("read", "1");
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Log.e("sms", "" + cursor.getInt(cursor.getColumnIndex("_id")) + "  ,  " + cursor.getString(cursor.getColumnIndex("address")));
                    int res = getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=?", new String[] { "" + cursor.getInt(cursor.getColumnIndex("_id")) });
                    Log.e("sms","geng xin = "+res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    /**
     * 获取短信
     * @param context
     *
     */
    public void getSms(Context context){
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "person","body", "date", "type" };
        //String where = " address = "+tel_number;
        Cursor cur = cr.query(Uri.parse("content://sms/"), projection, null, null, "date desc");
        if (null == cur)
            return;
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));// 短信内容
            String date = cur.getString(cur.getColumnIndex("date"));// 日期
            String type = cur.getString(cur.getColumnIndex("type"));// 收发类型 1表示接受 2表示发送
            Date d = new Date(Long.parseLong(date));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = dateFormat.format(d);
            Log.e("sms","number:"+number+"name:"+name+"body:"+body+"\n"
                    +"date:"+time+"typeColumn:"+type);
        }
    }
}
