package com.jinoolee.cuebap.Data;

public class CurrentUser {

    private String name;
    private String email;
    private String phone;
    private String school;
    private String campus;

    private static final CurrentUser currentUser = new CurrentUser();

    private CurrentUser(){}

    public static CurrentUser getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        name = user.getName();
        email= user.getEmail();
        phone = user.getPhone();
        school = user.getSchool();
        campus = user.getCampus();
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
