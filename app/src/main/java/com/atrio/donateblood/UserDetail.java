package com.atrio.donateblood;

/**
 * Created by Arpita Patel on 16-08-2017.
 */

public class UserDetail {
    public  String name;
    public String age;
    public String weight;
    public String gender;
    public String state;
    public String bloodgroup;
    public String country;
    public String city;
    public String timeperiod;
    public String emailid;
    public String phoneno;
    public String count;
    public UserDetail() {
    }

    public UserDetail(String name, String age, String weight, String gender, String state, String bloodgroup, String country, String city, String timeperiod,String emailid,String phoneno,String count) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.state = state;
        this.bloodgroup = bloodgroup;
        this.country = country;
        this.city = city;
        this.timeperiod = timeperiod;
        this.emailid=emailid;
        this.phoneno=phoneno;
        this.count=count;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimeperiod() {
        return timeperiod;
    }

    public void setTimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }



}
