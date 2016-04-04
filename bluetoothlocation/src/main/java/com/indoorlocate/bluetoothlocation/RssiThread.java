package com.indoorlocate.bluetoothlocation;

import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Xuwen on 2016/4/3.
 */
public class RssiThread extends Thread {
    public static Handler rssiHandler;
     BluetoothGatt mbluetootGatt;
     RssiThread(BluetoothGatt bluetoothGatt){
       this.mbluetootGatt=bluetoothGatt;
    }
    @Override
    public void run() {
            Looper.prepare();
            Log.v("MainActivity", "获取rssi的线程已经启用");
            Log.v("MainActivity",mbluetootGatt.toString());
            rssiHandler=new Handler(){
               @Override
               public void handleMessage(Message msg) {
                  if(msg.what==0x123){
                      Log.v("MainActivity",msg.getData().getString("inf"));
                  }
               }
           };
        Looper.loop();
        }
    }
