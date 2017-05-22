package com.sweetcompany.sweetie.Registration;


public class UserVM {
    private String username;
    private String phone;
    private boolean gender;



    private String mail;

    UserVM(){}

    UserVM(String u, String p, String m, boolean g){
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
}
