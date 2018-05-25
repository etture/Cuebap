package com.jinoolee.cuebap.SignInCreateAccount;

public class CampusListItem {

    private int school;
    private int campus;

    public CampusListItem(int school, int campus){

        this.school = school;
        this.campus = campus;

    }

    public void setSchool(int school){

        this.school = school;

    }

    public int getSchool(){

        return school;

    }

    public void setCampus(int campus){

        this.campus = campus;

    }


    public int getCampus(){

        return campus;

    }

}
