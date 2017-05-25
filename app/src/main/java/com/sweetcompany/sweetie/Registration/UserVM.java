package com.sweetcompany.sweetie.Registration;


import com.sweetcompany.sweetie.Firebase.SweetUser;

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
    UserVM(SweetUser sweetUser){

        this.username = sweetUser.getUsername();
        this.phone = sweetUser.getPhone();
        this.gender = sweetUser.isGender();
        this.mail = sweetUser.getEmail();
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
