package com.wustor.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //由AIDL文件生成的Java类
    private PeopleManager mPeopleManager;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    //包含Book对象的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("pid---->client", String.valueOf(android.os.Process.myPid()));
    }


    public void addPeople(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            return;
        }
        if (mPeopleManager == null)
            return;
        People people = new People();
        people.setAge(18);
        people.setGender("male");
        people.setHobby("travel");
        try {
            mPeopleManager.addPeople(people);
            Log.e(getLocalClassName(), people.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getPeople(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Log.d("client-->", "正在连接Server");
            return;
        }
        if (mPeopleManager == null)
            return;
        try {
            Toast.makeText(this, String.valueOf(mPeopleManager.getPeople().size()), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setClass(this, AIDLService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPeopleManager = PeopleManager.Stub.asInterface(service);
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}
