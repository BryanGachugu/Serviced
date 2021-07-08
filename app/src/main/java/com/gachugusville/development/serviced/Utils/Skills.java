package com.gachugusville.development.serviced.Utils;

public class Skills {
    private String skill_identity;
    private String skill_pay_mode;
    private int skill_jobs_number;
    private double skill_price;
    private String skill_description;

    public Skills() {
        //Needed for firebase
    }

    public Skills(String skill_identity, String skill_pay_mode, int skill_jobs_number, double skill_price, String skill_description) {
        this.skill_identity = skill_identity;
        this.skill_pay_mode = skill_pay_mode;
        this.skill_jobs_number = skill_jobs_number;
        this.skill_price = skill_price;
        this.skill_description = skill_description;
    }

    public String getSkill_identity() {
        return skill_identity;
    }

    public void setSkill_identity(String skill_identity) {
        this.skill_identity = skill_identity;
    }

    public String getSkill_pay_mode() {
        return skill_pay_mode;
    }

    public void setSkill_pay_mode(String skill_pay_mode) {
        this.skill_pay_mode = skill_pay_mode;
    }

    public int getSkill_jobs_number() {
        return skill_jobs_number;
    }

    public void setSkill_jobs_number(int skill_jobs_number) {
        this.skill_jobs_number = skill_jobs_number;
    }

    public double getSkill_price() {
        return skill_price;
    }

    public void setSkill_price(double skill_price) {
        this.skill_price = skill_price;
    }

    public String getSkill_description() {
        return skill_description;
    }

    public void setSkill_description(String skill_description) {
        this.skill_description = skill_description;
    }
}
