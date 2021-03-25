package com.company.pojo;

public class User {
    private String username;
    private String password;
    private String telephone;

    public User(String username, String password, String telephone) {
        this.username = username;
        this.password = password;
        this.telephone = telephone;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
