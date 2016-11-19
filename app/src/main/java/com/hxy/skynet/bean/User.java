package com.hxy.skynet.bean;

/**
 * Created by ASD on 2016/9/5.
 */
public class User {
    public String name, psd, company, phone, email, sheng, shi, address;
    public User(){}

    public User( String name, String psd, String company,
                String phonenum, String email, String shen, String shi, String address) {
        this.name = name;
        this.psd = psd;
        this.company = company;
        this.phone = phonenum;
        this.email = email;
        this.sheng = shen;
        this.shi = shi;
        this.address = address;
    }
}
