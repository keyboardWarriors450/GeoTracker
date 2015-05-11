/*
 * Copyright (c) 2015.
 * Viet Nguyen
 * David Kim
 * Daniel Khieuson
 * Alex Hong
 *
 * TCSS 450
 */

package com.mycompany.geotracker.model;

/**
 * Created by viet on 4/16/2015.
 *
 * Hold user's information, user account...
 */
public class User {

    /**
     * Representing user ID
     */
    private String userID;

    /**
     * Representing user email
     */
    private String email;

    /**
     * Representing user password
     */
    private String password;

    /**
     * Hold secret question from user
     */
    private String question;

    /**
     * Hold user's secret answer
     */
    private String answer;

    public static final String UID = "uid", EMAIL = "email", PASSWORD = "password",
            QUESTION = "question", ANSWER = "answer";

    /**
     * This constructing user account base on inputs
     *
     * @param userID is user input ID as email address - Unique ID for creating userId
     * @param email is user email address
     * @param password is password user enter create
     * @param question is secret question
     * @param answer is secret answer from user input
     */
    public User(String userID, String email, String password, String question, String answer) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.question = question;
        this.answer = answer;
    }

    /**
     *
     * @return user ID as email address
     */
    public String getUserID() {
        return userID;
    }

    /**
     *
     * @return user email address
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return password when reset
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return secret question
     */
    public String getQuestion() {
        return question;
    }

    /**
     *
     * @return secrete answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * saving current user information
     */
    public void saveReference() {

    }

    /**
     * reset user information
     */
    public void clear() {

    }

//    @Override
//    public String toString() {
//        return "com.mycompany.geotracker.model.User{" +
//                "email='" + email + '\'' +
//                "password='" + password + '\'' +
//                "question='" + question + '\'' +
//                "answer='" + answer + '\'' +
//                '}';
//    }
}
