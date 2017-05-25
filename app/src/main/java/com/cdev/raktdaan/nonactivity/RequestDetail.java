package com.cdev.raktdaan.nonactivity;

/**
 * Created by ash on 23/05/17.
 */

public class RequestDetail {
    private String name;
    private String age;
    private String gender;
    private String bloodGroup;
    private String bloodUnits;
    private String urgency;
    private String time;
    private String email;
    private String key;
    private String accepted;

    public RequestDetail() {
    }


    public RequestDetail(String name, String age, String gender, String bloodGroup, String bloodUnits, String urgency, String time, String email, String key,String accepted) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.bloodUnits = bloodUnits;
        this.urgency = urgency;
        this.gender = gender;
        this.time = time;
        this.email = email;
        this.key = key;
        this.accepted = accepted;
    }

    public String getTime() {
        return time;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {

        return name;
    }

    public String getAge() {
        return age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getBloodUnits() {
        return bloodUnits;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public String getAccepted() {
        return accepted;
    }
}
