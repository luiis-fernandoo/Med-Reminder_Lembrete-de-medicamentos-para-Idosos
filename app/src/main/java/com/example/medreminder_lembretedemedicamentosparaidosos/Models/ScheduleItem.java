package com.example.medreminder_lembretedemedicamentosparaidosos.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleItem implements Parcelable {
    private String time;
    private String quantity;

    public ScheduleItem(String time, String quantity) {
        this.time = time;
        this.quantity = quantity;
    }

    protected ScheduleItem(Parcel in) {
        time = in.readString();
        quantity = in.readString();
    }

    public static final Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
        @Override
        public ScheduleItem createFromParcel(Parcel in) {
            return new ScheduleItem(in);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };

    public String getTime() {
        return time;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(quantity);
    }
}
