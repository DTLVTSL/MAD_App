package com.example.mad.lab2;

/**
 * Created by daniel on 01/04/17.
 */


public class members_class {
    public int image;
    public String name;
    public String number;

    //Constructor => in case we dont receive anything
    public members_class(){
        super();
    }

    //Constructor => in case we receive elements
    public members_class(int image, String name, String number){
        super();
        this.image = image;
        this.name = name;
        this.number = number;

    }
}

///////////////////////////////
//Elements will be of this class. Data type: members_class
