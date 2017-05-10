package com.example.mad.lab2;

/**
 * Created by daniel on 04/04/17.
 */

public class items_class {
    public int icon;
    public String name;
    public String alert;
    public String price;
    public String currency;

    //Constructor => in case we dont receive anything
    public items_class() {
        super();
    }

    //Constructor => in case we receive elements
    public items_class(String name, String price, String currency, String alert, int icon) {
        super();
        this.icon = icon;
        this.name = name;
        this.alert = alert;
        this.price = price;
        this.currency= currency;
    }


    public items_class(String name, String price, int icon) {
        super();
        this.icon = icon;
        this.name = name;
        this.alert = "";
        this.price = price;
        this.currency= "€";
    }

    public items_class(String name, String price, String currency, int icon) {
        super();
        this.icon = icon;
        this.name = name;
        this.alert = "";
        this.price = price;
        this.currency= currency;
    }
    public items_class(String name, String price, String currency) {
        super();
        this.name = name;
        this.alert = "";
        this.price = price;
        this.currency= currency;
    }

    public void setName(String name){this.name=name;}
    public void setAlert(String alert){this.alert=alert;}
    public void setPrice(String price){this.price=price;}
    public void setCurrency(String currency){this.currency=currency;}
    public void setIcon(int icon){this.icon=icon;}

    public String getName()     {return this.name;}
    public String getAlert()    {return this.alert;}
    public String getPrice()     {return this.price;}
    public String getCurrency() {return this.currency;}
    public int getIcon()     {return this.icon;}


}
