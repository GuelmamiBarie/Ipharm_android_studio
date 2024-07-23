package com.example.ipharm.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Selled implements Parcelable
{
    private String nameMS;
    private String dateOfStoringS;
    private int stabilityMS;
    private int nbBottlesS;
    private Double remainingPartMS;
    private Double theAmount;

    public Selled(String nameMS, String dateOfStoringS, int stabilityMS, int nbBottlesS, Double remainingPartMS, Double theAmount)
    {
        this.nameMS = nameMS;
        this.dateOfStoringS = dateOfStoringS;
        this.stabilityMS = stabilityMS;
        this.nbBottlesS = nbBottlesS;
        this.remainingPartMS = remainingPartMS;
        this.theAmount = theAmount;
    }

    protected Selled(Parcel in)
    {
        nameMS = in.readString();
        dateOfStoringS = in.readString();
        stabilityMS = in.readInt();
        nbBottlesS = in.readInt();
        if (in.readByte() == 0) {
            remainingPartMS = null;
        } else {
            remainingPartMS = in.readDouble();
        }
        if (in.readByte() == 0) {
            theAmount = null;
        } else {
            theAmount = in.readDouble();
        }
    }

    public static final Creator<Selled> CREATOR = new Creator<Selled>() {
        @Override
        public Selled createFromParcel(Parcel in) {
            return new Selled(in);
        }

        @Override
        public Selled[] newArray(int size) {
            return new Selled[size];
        }
    };

    public String getNameMS() {
        return nameMS;
    }

    public void setNameMS(String nameMS) {
        this.nameMS = nameMS;
    }

    public String getDateOfStoringS() {
        return dateOfStoringS;
    }

    public void setDateOfStoringS(String dateOfStoringS) {
        this.dateOfStoringS = dateOfStoringS;
    }

    public int getStabilityMS() {
        return stabilityMS;
    }

    public void setStabilityMS(int stabilityMS) {
        this.stabilityMS = stabilityMS;
    }

    public int getNbBottlesS() {
        return nbBottlesS;
    }

    public void setNbBottlesS(int nbBottlesS) {
        this.nbBottlesS = nbBottlesS;
    }

    public Double getRemainingPartMS() {
        return remainingPartMS;
    }

    public void setRemainingPartMS(Double remainingPartMS) {
        this.remainingPartMS = remainingPartMS;
    }

    public Double getTheAmount() {
        return theAmount;
    }

    public void setTheAmount(Double theAmount) {
        this.theAmount = theAmount;
    }

    @Override
    public String toString()
    {
        return "Selled{" +
                "nameMS='" + nameMS + '\'' +
                ", dateOfStoringS='" + dateOfStoringS + '\'' +
                ", stabilityMS=" + stabilityMS +
                ", nbBottlesS=" + nbBottlesS +
                ", remainingPartMS=" + remainingPartMS +
                ", theAmount=" + theAmount +
                '}';
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(nameMS);
        dest.writeString(dateOfStoringS);
        dest.writeInt(stabilityMS);
        dest.writeInt(nbBottlesS);
        if (remainingPartMS == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(remainingPartMS);
        }
        if (theAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(theAmount);
        }
    }
}