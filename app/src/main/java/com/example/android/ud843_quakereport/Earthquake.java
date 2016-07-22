package com.example.android.ud843_quakereport;

/**
 * Created by OWNER on 7/22/2016.
 */
public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private String mRelativeLocation;
    private long mDate;
    private String mUrl;

    public Earthquake(double magnitude,String relativeLocation, String location, long date, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mRelativeLocation = relativeLocation;
        mUrl = url;
    }

    public double getMagnitude(){
        return mMagnitude;
    }

    public String getLocation(){
        return mLocation;
    }

    public long getDate(){
        return mDate;
    }

    public String getRelativeLocation(){
        return mRelativeLocation;
    }

    public String getUrl(){
        return mUrl;
    }
}
