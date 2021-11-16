package com.pafex.zscs;

import java.io.Serializable;

public class ServiceDetail implements Serializable {
    private String min_weight;
    private int image;
    private double rto_charge;

    public ServiceDetail(String min_weight, double rto_charge,int image) {
        this.min_weight = min_weight;
        this.rto_charge = rto_charge;
        this.image=image;
    }

    public String getMin_weight() {return min_weight;}
    public double getRto_charge() {return rto_charge;}
    public int getImage() {return image;}

}