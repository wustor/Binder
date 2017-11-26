package com.wustor.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();

    //包含People对象的list
    private List<People> mPeoples = new ArrayList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPeopleManager;
    }

    //由AIDL文件生成的PeopleManager
    private final PeopleManager.Stub mPeopleManager = new PeopleManager.Stub() {
        @Override
        public List<People> getPeople() throws RemoteException {
            synchronized (this) {
                if (mPeoples != null) {
                    return mPeoples;
                }
                return new ArrayList<>();
            }
        }


        @Override
        public void addPeople(People book) throws RemoteException {
            synchronized (this) {
                if (mPeoples == null) {
                    mPeoples = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "People is null in In");
                }
                mPeoples.add(book);
            }
        }
    };


}
