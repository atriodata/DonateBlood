package com.atrio.donateblood.model;

/**
 * Created by Arpita Patel on 03-10-2017.
 */

public class ItemData {

    String country_name,country_code;
    Integer flag_img;

    public ItemData(String country_name, String country_code, Integer flag_img) {
        this.country_name = country_name;
        this.country_code = country_code;
        this.flag_img = flag_img;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public Integer getFlag_img() {
        return flag_img;
    }

    public void setFlag_img(Integer flag_img) {
        this.flag_img = flag_img;
    }


}

