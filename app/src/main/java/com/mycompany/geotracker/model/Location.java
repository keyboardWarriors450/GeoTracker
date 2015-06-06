/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.model;

/**
 * Created by David on 5/8/2015.
 */
public class Location {

    private String uid;
    private String lat;
    private String lon;
    private String speed;
    private String heading;
    private long timestamp;

    /**
     * The constructor.
     * @param uid the user id
     * @param lat the latitude
     * @param lon the longitude
     * @param speed the speed
     * @param heading the heading
     * @param timestamp the timestamp
     */
    public Location(String uid, String lat, String lon, String speed, String heading,
                    long timestamp) {
        this.uid = uid;
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.heading = heading;
        this.timestamp = timestamp;
    }

    /**
     * Returns the latitude.
     * @return the latitude
     */
    public String getLat() {
        return lat;
    }

    /**
     * Resets the latitude.
     * @param lat the latitude
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Returns the longitude.
     * @return the longitude
     */
    public String getLon() {
        return lon;
    }

    /**
     * Resets the longitude
     * @param lon the longitude
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     * Returns the speed.
     * @return the speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Resets the speed.
     * @param speed the speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * Returns the heading.
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Resets the heading.
     * @param heading the heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     * Returns the timestamp.
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Resets the timestamp.
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the user id.
     * @return the user id
     */
    public String getUid() {

        return uid;
    }

    /**
     * Resets the user id.
     * @param uid the user id
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Returns the string value with the timestamp, latitude, longitude, and heading.
     * @return the string value with the timestamp, latitude, longitude, and heading
     */
    public String toString() {
        return timestamp + "  " + lat + "  " + lon + "  " + heading;
    }
}
