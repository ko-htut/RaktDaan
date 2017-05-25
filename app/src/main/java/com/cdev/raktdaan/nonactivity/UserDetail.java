package com.cdev.raktdaan.nonactivity;

/**
 * Created by ash on 16/05/17.
 */

public class UserDetail {
    private String bloodGroup;
    private String gender;
    private  String dateOfBirth;
    private String mobileNumber;
    private String localAddress;


    public UserDetail() {
    }

    public UserDetail(String bloodGroup, String gender, String dateOfBirth, String mobileNumber, String localAddress) {
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.mobileNumber = mobileNumber;
        this.localAddress = localAddress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getLocalAddress() {
        return localAddress;
    }
}
