package com.gachugusville.development.serviced.Utils;

public class JobRequestModel {

    private String customerUid;
    private String customer_userName;
    private String job_status; //Ongoing, completed, Canceled, rejected
    private String paymentType; // Hour, Job, day?
    private double payValue;

    public JobRequestModel(String customerUid, String customer_userName, String job_status, String paymentType, double payValue) {
        this.customerUid = customerUid;
        this.customer_userName = customer_userName;
        this.job_status = job_status;
        this.paymentType = paymentType;
        this.payValue = payValue;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getCustomer_userName() {
        return customer_userName;
    }

    public void setCustomer_userName(String customer_userName) {
        this.customer_userName = customer_userName;
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
}
