package com.dataservicios.ttauditpromotoriabayer.Model;

/**
 * Created by usuario on 23/03/2015.
 */
public class User {

    private int  id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id= id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
