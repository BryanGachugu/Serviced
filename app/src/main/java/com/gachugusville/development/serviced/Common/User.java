package com.gachugusville.development.serviced.Common;

import java.util.List;

public class User {

    public User() {
        //for Firebase
    }



    private String first_name, last_name, profile_picture_url, phone_number, email, country;
    double latitude, longitude;
    private float rating;
    List<String> reviews;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User(String first_name, String last_name, String profile_picture_url, String phone_number, String email, String country,
                double longitude, double latitude, float rating, List<String> reviews) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_picture_url = profile_picture_url;
        this.phone_number = phone_number;
        this.email = email;
        this.rating = rating;
        this.reviews = reviews;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
