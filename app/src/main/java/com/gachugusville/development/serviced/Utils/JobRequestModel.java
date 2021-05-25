package com.gachugusville.development.serviced.Utils;

public class JobRequestModel {

    private String customerUid;
    private String client_profile_url;
    private String skill_identity;
    private String customer_userName;
    private String job_status; //Ongoing, completed, Canceled, rejected
    private String paymentType; // Hour, Job, day?
    private double payValue;
    private double job_rating;

    public JobRequestModel(String customerUid, String client_profile_url, String customer_userName,String skill_identity, String job_status, String paymentType, double payValue, double job_rating) {
        this.customerUid = customerUid;
        this.client_profile_url = client_profile_url;
        this.customer_userName = customer_userName;
        this.skill_identity = skill_identity;
        this.job_status = job_status;
        this.paymentType = paymentType;
        this.payValue = payValue;
        this.job_rating = job_rating;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getClient_profile_url() {
        return client_profile_url;
    }

    public void setClient_profile_url(String client_profile_url) {
        this.client_profile_url = client_profile_url;
    }

    public String getCustomer_userName() {
        return customer_userName;
    }

    public void setCustomer_userName(String customer_userName) {
        this.customer_userName = customer_userName;
    }

    public String getSkill_identity() {
        return skill_identity;
    }

    public void setSkill_identity(String skill_identity) {
        this.skill_identity = skill_identity;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getPayValue() {
        return payValue;
    }

    public void setPayValue(double payValue) {
        this.payValue = payValue;
    }

    public double getJob_rating() {
        return job_rating;
    }

    public void setJob_rating(double job_rating) {
        this.job_rating = job_rating;
    }
}
