package com.indoorlocate.bluetoothlocation;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public class RssiThread1 extends Thread {
        @Override
        public void run() {
            Log.v("MainActivity","读取一次"+mbluetootGatt1.toString()+"的RSSI");
           // while(true){
            for(int i=0;i<30;i++){
                mbluetootGatt1.readRemoteRssi();
                mbluetootGatt2.readRemoteRssi();
                mbluetootGatt3.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(100);
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
            for(int i=0;i<300;i++){
                mbluetootGatt2.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(100);
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
            for(int i=0;i<300;i++){
                mbluetootGatt3.readRemoteRssi();
                try {
                    Thread.currentThread().sleep(100);
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
    Button connect1,connect2,connect3,search,stopsearch;
    String str1,str2,str3;
    BluetoothDevice device1;
    BluetoothDevice device2;
    BluetoothDevice device3;
    BluetoothGatt mbluetootGatt1;
    BluetoothGatt mbluetootGatt2;
    BluetoothGatt mbluetootGatt3;
    BluetoothLeScanner mBluetoothLeScanner;
    ScanSettings bleScanSettings;
    List<ScanFilter> bleScanFilters;
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
        connect1=(Button)findViewById(R.id.connect1);
        connect2=(Button)findViewById(R.id.connect2);
        connect3=(Button)findViewById(R.id.connect3);
        search=(Button)findViewById(R.id.search);
        stopsearch=(Button)findViewById(R.id.stopsearch);
        str1="00:15:83:00:3D:13";
        str2="00:15:83:00:40:D9";
        str3="00:15:83:00:3D:B2";
        //添加六个按钮的单击事件
        mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bleScanFilters = new ArrayList<>();
        bleScanFilters.add(
                new ScanFilter.Builder().setDeviceAddress("20:91:48:32:23:30").build()
        );
       /* bleScanFilters.add(
                new ScanFilter.Builder().setDeviceAddress("00:15:83:00:3D:13").build()
        );
        bleScanFilters.add(
                new ScanFilter.Builder().setDeviceAddress("00:15:83:00:40:D9").build()
        );
        bleScanFilters.add(
                new ScanFilter.Builder().setDeviceAddress("00:15:83:00:3D:B2").build()
        );*/
        bleScanSettings =new ScanSettings.Builder().build();
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
        /*device1 = bluetoothAdapter.getRemoteDevice(str1);
        mbluetootGatt1=device1.connectGatt(MainActivity.this, true, gattCallback);
        device2 = bluetoothAdapter.getRemoteDevice(str2);
        mbluetootGatt2=device2.connectGatt(MainActivity.this, true, gattCallback);
        device3 = bluetoothAdapter.getRemoteDevice(str3);
        mbluetootGatt3=device3.connectGatt(MainActivity.this, true, gattCallback);*/
        connect1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                device1 = bluetoothAdapter.getRemoteDevice(str1);
                mbluetootGatt1=device1.connectGatt(MainActivity.this, true, gattCallback);
            }
        });
        connect2.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                device2 = bluetoothAdapter.getRemoteDevice(str2);
                mbluetootGatt2=device2.connectGatt(MainActivity.this, true, gattCallback);
            }
        });
        connect3.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                device3 = bluetoothAdapter.getRemoteDevice(str3);
                mbluetootGatt3=device3.connectGatt(MainActivity.this, true, gattCallback);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // bluetoothAdapter.startLeScan(mLeScanCallback);
                // add a filter to only scan for advertisers with the given service UUID
               // Log.d(TAG, "Starting scanning with settings:" + bleScanSettings + " and filters:" + bleScanFilters);
                // tell the BLE controller to initiate scan
                mBluetoothLeScanner.startScan(bleScanFilters, bleScanSettings, mLeScanCallback);
            }
        });
        stopsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothLeScanner != null) {
                    Log.d(TAG, "Stop scanning.");
                    mBluetoothLeScanner.stopScan(mLeScanCallback);
                    //bluetoothAdapter.stopLeScan(mLeScanCallback);
                }

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
            if(gatt==mbluetootGatt1){
            Log.v("MainActivity",""+(200+rssi));}
            else if(gatt==mbluetootGatt2){
                Log.v("MainActivity",""+(400+rssi));
            }else{
                Log.v("MainActivity",""+(600+rssi));
            }
    }
    };
    //搜索回调函数
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult (int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.i(TAG, "Device name: " + device.getName()+ " Device address: " + device.getAddress() +" RSSI "+result.getRssi());
        }
    };
    //另外一个回调函数
   /* private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,final byte[] scanRecord)
        {
            Log.i(TAG, "name:" + device.getName()
                    + ",add:" + device.getAddress()
                    + ",type:" + device.getType()
                    + ",bondState:" + device.getBondState()
                    + ",rssi:" + rssi);
        }
    };*/
}