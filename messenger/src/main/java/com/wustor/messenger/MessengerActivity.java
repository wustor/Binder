package com.wustor.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MessengerActivity extends AppCompatActivity {
    private Messenger mService;
    private static final int MESS_ADD_PEOPLE = 0;
    private static final int MESS_GET_PEOPLE = 1;
    private static final int MESS_FROM_SERVER = 2;
    private boolean isBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

    }

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setClass(this, MessengerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addPeople(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!isBound) {
            attemptToBindService();
            return;
        }
        People people = new People();
        people.setAge(18);
        people.setGender("male");
        people.setHobby("travel");
        Message message = Message.obtain(null, MESS_ADD_PEOPLE);
        Bundle bundle = new Bundle();
        bundle.putParcelable("people", people);
        message.setData(bundle);
        //Messenger用来接收服务端发来的信息
        message.replyTo = messenger;
        try {
            //将Message发送到服务端
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void getPeople(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!isBound) {
            attemptToBindService();
            Log.d("client-->", "正在连接Server");
            return;
        }
        Message message = Message.obtain(null, MESS_GET_PEOPLE);
        //Messenger用来接收服务端发来的信息
        message.replyTo = messenger;
        try {
            //将Message发送到服务端
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            mService = new Messenger(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isBound = false;
        }
    };

    Messenger messenger = new Messenger(new ClientHandler());

    public class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESS_FROM_SERVER:
                    //接收服务器传过来的值
                    Bundle bundle = msg.getData();
                    int peopleSize = bundle.getInt("server");
                    Toast.makeText(MessengerActivity.this, String.valueOf(peopleSize), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
