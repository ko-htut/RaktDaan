package com.cdev.raktdaan.Fragments;

/**
 * Created by ash on 25/05/17.
 */

public class ArrayListDetail {
    private String email,name,mobileNumber;

    public ArrayListDetail() {
    }

    public ArrayListDetail(String email, String name, String mobileNumber) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }
}
