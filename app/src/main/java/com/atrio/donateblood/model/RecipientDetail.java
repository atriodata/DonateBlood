package com.atrio.donateblood.model;

/**
 * Created by Arpita Patel on 31-08-2017.
 */

public class RecipientDetail {
    public String bloodgroup;
    public String req_date;
    public String state;
    public String country;
    public String city;
    public String other_detail;
    public String emailid;
    public String phoneno;
    public String msg_id;
    public String body;
    public String type;


    public RecipientDetail() {

    }

    public RecipientDetail(String bloodgroup, String req_date, String state, String country, String city, String other_detail, String emailid, String phoneno, String msg_id, String body) {
        this.bloodgroup = bloodgroup;
        this.req_date = req_date;
        this.state = state;
        this.country = country;
        this.city = city;
        this.other_detail = other_detail;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.msg_id = msg_id;
        this.body = body;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getOther_detail() {
        return other_detail;
    }

    public void setOther_detail(String other_detail) {
        this.other_detail = other_detail;
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

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
