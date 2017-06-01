package com.sweetcompany.sweetie.Registration;


import com.sweetcompany.sweetie.Firebase.SweetUser;

public class UserVM {
    private String key;
    private String username;
    private String phone;
    private boolean gender;
    private String mail;

    UserVM(){}

    UserVM(String k,String u, String p, String m, boolean g){
        this.key = k;
        this.username = u;
        this.phone = p;
        this.mail = m;
        this.gender = g;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() { return mail; }

    public boolean isGender() {
        return gender;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
