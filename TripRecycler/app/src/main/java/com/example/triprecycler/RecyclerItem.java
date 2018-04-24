package com.example.triprecycler;

/**
 * Created by hend on 02/04/18.
 */

public class RecyclerItem {
    private String tripTitle;
    private String tripDate;

    public RecyclerItem(String tripTitle, String tripDate) {
        this.tripTitle = tripTitle;
        this.tripDate = tripDate;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}
