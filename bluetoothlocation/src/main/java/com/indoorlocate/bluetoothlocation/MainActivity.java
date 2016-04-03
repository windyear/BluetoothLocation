package com.indoorlocate.bluetoothlocation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Button bt1;
    Button btrssi1;
    Button btrssi2;
    TextView RSSI;
    ProgressBar progressBar;
    ListView b_listView;
    ArrayAdapter<String> adt_Devices;
    int mark;
    int num[];
    List<String> lst_Devices = new ArrayList<>();
    BluetoothGatt mBluetoothGatt1;
    BluetoothGatt mBluetoothGatt2;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            // 显示所有收到的消息及其细节
            BluetoothDevice bluetooth_Device ;
            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                bluetooth_Device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bluetooth_Device.getBondState() == BluetoothDevice.BOND_NONE) {
                    String str ="设备|"+bluetooth_Device.getName() + "|"
                            + bluetooth_Device.getAddress();
                    if (lst_Devices.indexOf(str) == -1)// 防止地址被重复添加
                        lst_Devices.add(str); // 获取设备名称和mac地址
                    adt_Devices.notifyDataSetChanged();
                }
                else if(bluetooth_Device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    String str ="已配对|"+bluetooth_Device.getName() + "|"
                            + bluetooth_Device.getAddress();
                    if (lst_Devices.indexOf(str) == -1)// 防止地址被重复添加
                        lst_Devices.add(str); // 获取设备名称和mac地址
                    adt_Devices.notifyDataSetChanged();
                }
            }
            bluetoothAdapter.cancelDiscovery();
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!bluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 1;
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "已经打开了蓝牙，可以正常使用APP", Toast.LENGTH_LONG);
            toast.show();
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bt1 = (Button) findViewById(R.id.button);
        RSSI=(TextView)findViewById(R.id.Rssi);
        btrssi1=(Button)findViewById(R.id.RSSIbutton1);
        btrssi2=(Button)findViewById(R.id.RSSIbutton2);
        b_listView = (ListView) this.findViewById(R.id.lvDevices);
        adt_Devices = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lst_Devices);
        b_listView.setAdapter(adt_Devices);
        b_listView.setOnItemClickListener(new ItemClickEvent());
        // 注册Receiver来获取蓝牙设备相关的结果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, intent);
        num=new int[3000];
        btrssi1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "点击了读取");
                for(mark=0;mark<3000;) {
                    mBluetoothGatt1.readRemoteRssi();
                   /* try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    //Log.v(TAG,"线程已经暂停一秒");
                }
                for(mark=0;mark<3000;mark++){
                    Log.v(TAG,""+num[mark]+" "+mark);
                }
                RSSI.setText(""+mark);
                Toast.makeText(MainActivity.this,"读取完毕",Toast.LENGTH_SHORT).show();
            }
        });
        btrssi2.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "点击了读取");
                for(mark=0;mark<3000;) {
                    mBluetoothGatt2.readRemoteRssi();
                   /* try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    //Log.v(TAG,"线程已经暂停一秒");
                }
                for(mark=0;mark<3000;mark++){
                    Log.v(TAG,""+num[mark]+" "+mark);
                }
                RSSI.setText(""+mark);
                Toast.makeText(MainActivity.this,"读取完毕",Toast.LENGTH_SHORT).show();
            }
        });
    }
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
        //解除注册
        unregisterReceiver(mReceiver);
    }

    public void onClick_Search(View v) {
        // 如果正在搜索，就先取消搜索
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
            progressBar.setVisibility(View.VISIBLE);
            bluetoothAdapter.startDiscovery();
        }
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
            String str = lst_Devices.get(arg2);
            String[] values = str.split("\\|");
            String address = values[2];
            BluetoothDevice btDev = bluetoothAdapter.getRemoteDevice(address);
            if(arg2==0){
            mBluetoothGatt1 = btDev.connectGatt(MainActivity.this, false, gattCallback);}
            else { mBluetoothGatt2 = btDev.connectGatt(MainActivity.this, false, gattCallback);}

        }
    }

    //实现gattCallback
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback()
    {

        @Override
        public void onConnectionStateChange(BluetoothGatt mBluetoothGatt, int status, int newState)
        {
            //设备连接状态改变会回调这个函数
            Log.v(TAG, "回调函数已经调用");
            super.onConnectionStateChange(mBluetoothGatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                //连接成功, 可以把这个gatt 保存起来, 需要读rssi的时候就
               Toast.makeText(MainActivity.this, "已经连接", Toast.LENGTH_LONG).show();
                Log.v(TAG, "回调函数已经调用");
            }
        }


        @Override
        //底层获取RSSI后会回调这个函数
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            //判断是否读取成功
            if(rssi!=0)
            {
                num[mark]=rssi;
                mark++;
            }
        }

    };
}