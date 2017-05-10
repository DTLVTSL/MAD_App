package com.example.mad.lab2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by daniel on 01/04/17.
 */


public class groups_class {
    public int icon;
    public String title;
    public String noti;
    public String groupID;
    private HashMap<String, Object> Items;
    private ArrayList<String> members = new ArrayList<>();

    //Constructor => in case we dont receive anything
    public groups_class(){
        super();
    }

    //Constructor => in case we receive elements
    public groups_class(String groupID, String noti,int icon, String title, HashMap<String, Object> Items){
        super();
        this.icon = icon;
        this.title = title;
        this.noti = noti;
        this.Items =Items;
        this.groupID=groupID;

    }



    public void setIcon( int icon){this.icon=icon;}
    public int getIcon(){return this.icon;}

    public void setTitle(String title){this.title=title;}
    public String getTitle(){return title;}

    public void setNoti(String noti){this.noti=noti;}
    public String getNoti(){return noti;}

    public void setItems_list(HashMap<String, Object> items_list){this.Items =Items;}
    public HashMap<String, Object> getItems_list(){return Items;}

    public void setGroupID(String groupID){this.groupID=groupID;}
    public String getGroupID(){return groupID;}
}

///////////////////////////////
//Elements will be of this class. Data type: groups_class

