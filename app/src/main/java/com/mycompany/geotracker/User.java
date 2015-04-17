package com.mycompany.geotracker;

/**
 * Created by viet on 4/16/2015.
 */
public class User {
    private String userID;
    private String email;
    private String password;
    private String secretQuestion;
    private String secretAnswer;

    public User(String userID, String email, String password, String secretQuestion, String secretAnswer) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void saveReference() {

    }

    public void clear() {

    }
}
