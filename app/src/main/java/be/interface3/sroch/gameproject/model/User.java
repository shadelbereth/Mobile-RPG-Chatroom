package be.interface3.sroch.gameproject.model;

import java.util.ArrayList;

/**
 * Created by s.roch on 30/09/2016.
 */
public class User extends DatabaseElement {
    long id;
    String username;
    String password;
    String email;
    String presentation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
