package com.jinoolee.cuebap.Data;

public class User {

    public String name;
    public String email;
    public String phone;
    public String school;
    public String campus;

    public User(){

    }

    public User(String name, String email, String phone, String school, String campus){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.school = school;
        this.campus = campus;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public String getSchool(){
        return school;
    }

    public String getCampus(){
        return campus;
    }

}
