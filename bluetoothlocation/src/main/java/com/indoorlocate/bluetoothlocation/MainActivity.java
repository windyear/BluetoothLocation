package com.indoorlocate.bluetoothlocation;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    public class RssiThread1 extends Thread {
        @Override
        public void run() {
            Log.v("MainActivity","读取一次"+mbluetootGatt1.toString()+"的RSSI");
           // while(true){
            for(int i=0;i<30;i++){
                mbluetootGatt1.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
   public class RssiThread2 extends Thread {
        @Override
        public void run() {
            Log.v("MainActivity","读取一次"+mbluetootGatt2.toString()+"的RSSI");
            // while(true){
            for(int i=0;i<30;i++){
                mbluetootGatt2.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class RssiThread3 extends Thread {
        @Override
        public void run() {
            Log.v("MainActivity","读取一次"+mbluetootGatt3.toString()+"的RSSI");
            // while(true){
            for(int i=0;i<30;i++){
                mbluetootGatt3.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //声明变量
    private static final String TAG = MainActivity.class.getSimpleName();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Button bt1;
    Button btrssi1;
    Button btrssi2;
    Button btrssi3;
    Button btrssi4;
    String str1,str2,str3;
    BluetoothDevice device1;
    BluetoothDevice device2;
    BluetoothDevice device3;
    BluetoothGatt mbluetootGatt1;
    BluetoothGatt mbluetootGatt2;
    BluetoothGatt mbluetootGatt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //利用Intent请求获得蓝牙权限
        if (!bluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 1;
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "已经打开了蓝牙，可以正常使用APP", Toast.LENGTH_LONG);
            toast.show();
        }
        //找到对象对应控件
        btrssi3= (Button) findViewById(R.id.RssiThread);
        btrssi1=(Button)findViewById(R.id.RSSIbutton1);
        btrssi2=(Button)findViewById(R.id.RSSIbutton2);
        btrssi4=(Button)findViewById(R.id.allRssiThread);
        str1="78:A5:04:7A:54:FA";
        str2="20:91:48:32:26:60";
        str3="20:91:48:32:21:45";
        device1=bluetoothAdapter.getRemoteDevice(str1);
        mbluetootGatt1=device1.connectGatt(this, false, gattCallback);
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        device2=bluetoothAdapter.getRemoteDevice(str2);
        mbluetootGatt2=device2.connectGatt(this, false, gattCallback);
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        device3=bluetoothAdapter.getRemoteDevice(str3);
        mbluetootGatt3=device3.connectGatt(this, false, gattCallback);
        //添加两个按钮的单击事件
       btrssi1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "点击了读取");
                new RssiThread1().start();
                }
        });
        btrssi2.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "点击了读取");
                new RssiThread2().start();
            }
        });
        //该单击时间创建了新的线程并且启动了，发送了一个消息给新线程
        btrssi3.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                new RssiThread3().start();
            }
        });
        btrssi4.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                new RssiThread1().start();
                new RssiThread2().start();
                new RssiThread3().start();
            }
        });
    }
    //检查蓝牙是否打开
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast toast = Toast.makeText(MainActivity.this, "已经打开了蓝牙，可以正常使用APP", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast1 = Toast.makeText(MainActivity.this, "还没打开蓝牙，不能使用APP", Toast.LENGTH_LONG);
            toast1.show();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt mBluetoothGatt, int status, int newState)
        {
            //设备连接状态改变会回调这个函数
            // Log.v(TAG, "回调函数已经调用");
            super.onConnectionStateChange(mBluetoothGatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                //连接成功, 可以把这个gatt 保存起来, 需要读rssi的时候就
                Log.v("MainActivity", "回调函数已经调用");
            }
        }
        @Override
        //底层获取RSSI后会回调这个函数
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.v("MainActivity"+gatt.toString(),""+rssi);
    }
    };
}