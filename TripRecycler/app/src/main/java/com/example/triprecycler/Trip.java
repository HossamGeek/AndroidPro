package com.example.triprecycler;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hend on 02/04/18.
 */

public class Trip implements Parcelable{

    private String tripName;
    private String tripId;
    private String tripStation;
    private String tripDestination;
    private String startDate;
    private String startTime;
    private String tripNote;
    private String emailuser;
    private String tripStatus;
    public  Trip(){
    }

    public Trip(String keyId,String name, String date){
        this.tripId = keyId;
        this.tripName = name;
        this.startDate = date;
    }
    public Trip(String name,String station, String destination, String sDate, String sTime, String note,String keyid, String email,String status) {

        tripName = name;
        tripStation = station;
        tripDestination = destination ;
        startDate = sDate ;
        startTime = sTime ;
        tripNote = note ;
        tripId = keyid;
        emailuser = email;
        tripStatus = status;
    }





//    public Trip(String tripName, String tripId, String tripStation, String tripDestination, String startDate, String startTime, String tripNote, String emailuser, String tripStatus) {
//        this.tripName = tripName;
//        this.tripId = tripId;
//        this.tripStation = tripStation;
//        this.tripDestination = tripDestination;
//        this.startDate = startDate;
//        this.startTime = startTime;
//        this.tripNote = tripNote;
//        this.emailuser = emailuser;
//        this.tripStatus = tripStatus;
//    }

    protected Trip(Parcel in) {
        tripName = in.readString();
        tripId = in.readString();
        tripStation = in.readString();
        tripDestination = in.readString();
        startDate = in.readString();
        startTime = in.readString();
        tripNote = in.readString();
        emailuser = in.readString();
        tripStatus = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripStation() {
        return tripStation;
    }

    public void setTripStation(String tripStation) {
        this.tripStation = tripStation;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTripNote() {
        return tripNote;
    }

    public void setTripNote(String tripNote) {
        this.tripNote = tripNote;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tripName);
        dest.writeString(tripId);
        dest.writeString(tripStation);
        dest.writeString(tripDestination);
        dest.writeString(startDate);
        dest.writeString(startTime);
        dest.writeString(tripNote);
        dest.writeString(emailuser);
        dest.writeString(tripStatus);
    }
}
