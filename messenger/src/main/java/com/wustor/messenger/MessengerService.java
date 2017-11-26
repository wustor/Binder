package com.wustor.messenger;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
@SuppressLint("HandlerLeak")
public class MessengerService extends Service {
    private Handler mHandler;
    private static final int MESS_ADD_PEOPLE = 0;
    private static final int MESS_GET_PEOPLE = 1;
    private static final int MESS_FROM_SERVER = 2;
    private int size = 0;

    private Messenger messenger;

    public MessengerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(mHandler);
        //在这个线程中创建一个handler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msgFromClient) {
                super.handleMessage(msgFromClient);
                //获取一个新消息
                Message replyToClient = Message.obtain(msgFromClient);
                switch (msgFromClient.what) {
                    //根据Message.what来判断执行服务端的哪段代码
                    case MESS_ADD_PEOPLE:
                        size += 2;
                        break;
                    case MESS_GET_PEOPLE:
                        replyToClient.what = MESS_FROM_SERVER;
                        Bundle serverBundle = new Bundle();
                        serverBundle.putInt("server", size);
                        replyToClient.setData(serverBundle);
                        try {
                            msgFromClient.replyTo.send(replyToClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}
