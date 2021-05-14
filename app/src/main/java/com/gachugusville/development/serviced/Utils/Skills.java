package com.gachugusville.development.serviced.Utils;

public class Skills {
    private String payDurationType; //hour, day, job, contract
    private String skillName;
    private double payValue;
    private double experience;
    int num_of_jobs;

    public Skills() {
        //Needed for firebase
    }

    public Skills(String payDurationType, String skillName, double payValue, double experience, int num_of_jobs) {
        this.payDurationType = payDurationType;
        this.skillName = skillName;
        this.payValue = payValue;
        this.experience = experience;
        this.num_of_jobs = num_of_jobs;
    }

    public String getPayDurationType() {
        return payDurationType;
    }

    public void setPayDurationType(String payDurationType) {
        this.payDurationType = payDurationType;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public double getPayValue() {
        return payValue;
    }

    public void setPayValue(double payValue) {
        this.payValue = payValue;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getNum_of_jobs() {
        return num_of_jobs;
    }

    public void setNum_of_jobs(int num_of_jobs) {
        this.num_of_jobs = num_of_jobs;
    }
}
