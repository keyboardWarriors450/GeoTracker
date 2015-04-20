/*
 * Copyright (c) 2015.
 * Viet Nguyen
 * David Kim
 * Daniel Khieuson
 * Alex Hong
 *
 * TCSS 450
 */

package com.mycompany.geotracker;

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
    private String secretQuestion;

    /**
     * Hold user's secret answer
     */
    private String secretAnswer;

    /**
     * This constructing user account base on inputs
     *
     * @param userID is user input ID as email address - Unique ID for creating userId
     * @param email is user email address
     * @param password is password user enter create
     * @param secretQuestion is secret question
     * @param secretAnswer is secret answer from user input
     */
    public User(String userID, String email, String password, String secretQuestion, String secretAnswer) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
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
    public String getSecretQuestion() {
        return secretQuestion;
    }

    /**
     *
     * @return secrete answer
     */
    public String getSecretAnswer() {
        return secretAnswer;
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
}
