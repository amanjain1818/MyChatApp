package com.example.mychatapp.Model;

public class User {
    public User() {
    }

   private String mail, password, profilepic, username,  lastmessage, userid, status;
    private String phonenumber;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String mail, String password, String profilepic, String username, String lastmessage, String userid, String status, String phonenumber) {
        this.mail = mail;
        this.password = password;
        this.profilepic = profilepic;
        this.username = username;
        this.lastmessage = lastmessage;
        this.userid = userid;
        this.status = status;
        this.phonenumber = phonenumber;
    }

    // signup constructor
    public User(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;

    }
//signupConstructor

    public User(String username, String mail, String password, String phonenumber) {
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.phonenumber = phonenumber;
    }
}