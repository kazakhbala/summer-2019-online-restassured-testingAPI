package com.automation.tests.MyPractice;

public class SpartanPOJO {

    private String name;
    private String gender;
    private long phone;

    public SpartanPOJO(){

    }

    public SpartanPOJO(String name, String gender, long phone) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "SpartanPOJO{" +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone=" + phone +
                '}';
    }
}
