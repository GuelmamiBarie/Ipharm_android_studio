package com.example.ipharm.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Patient implements Parcelable
{
    private String nameP;
    private String phonenumber;
    private double sizeP;
    private double weightP;
    private double bodySurfaceP;
    private String profileImageP;

    public Patient(String nameP, String phonenumber, double sizeP, double weightP, double bodySurfaceP, String profileImageP)
    {
        this.nameP = nameP;
        this.phonenumber = phonenumber;
        this.sizeP = sizeP;
        this.weightP = weightP;
        this.bodySurfaceP = bodySurfaceP;
        this.profileImageP = profileImageP;
    }

    protected Patient(Parcel in) {
        nameP = in.readString();
        phonenumber = in.readString();
        sizeP = in.readDouble();
        weightP = in.readDouble();
        bodySurfaceP = in.readDouble();
        profileImageP = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getNameP() {
        return nameP;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public double getSizeP() {
        return sizeP;
    }

    public void setSizeP(double sizeP) {
        this.sizeP = sizeP;
    }

    public double getWeightP() {
        return weightP;
    }

    public void setWeightP(double weightP) {
        this.weightP = weightP;
    }

    public double getBodySurfaceP() {
        return bodySurfaceP;
    }

    public void setBodySurfaceP(double bodySurfaceP) {
        this.bodySurfaceP = bodySurfaceP;
    }

    public String getProfileImageP() {
        return profileImageP;
    }

    public void setProfileImageP(String profileImageP) {
        this.profileImageP = profileImageP;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "nameP='" + nameP + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", sizeP=" + sizeP +
                ", weightP=" + weightP +
                ", bodySurfaceP=" + bodySurfaceP +
                ", profileImageP='" + profileImageP + '\'' +
                '}';
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameP);
        dest.writeString(phonenumber);
        dest.writeDouble(sizeP);
        dest.writeDouble(weightP);
        dest.writeDouble(bodySurfaceP);
        dest.writeString(profileImageP);
    }
}