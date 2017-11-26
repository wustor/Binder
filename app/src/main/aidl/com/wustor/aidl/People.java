package com.wustor.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author pangchao
 * created on 2017/11/25
 * email fat_chao@163.com.
 */

public class People implements Parcelable {
    private int age;
    private String gender;
    private String hobby;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public People() {
    }

    protected People(Parcel in) {
        age = in.readInt();
        gender = in.readString();
        hobby = in.readString();
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(hobby);
    }

    @Override
    public String toString() {
        return "People{" +
                "age=" + age +
                ", gender='" + gender + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
