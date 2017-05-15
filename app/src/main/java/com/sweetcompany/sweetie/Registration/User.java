package com.sweetcompany.sweetie.Registration;


public class User {
    private String username;
    private String phone;
    private boolean gender;

    User(){}

    User(String u, String p, boolean g){
        this.username=u;
        this.phone=p;
        this.gender=g;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isGender() {
        return gender;
    }
}
