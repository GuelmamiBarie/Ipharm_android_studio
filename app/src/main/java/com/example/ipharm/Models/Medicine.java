package com.example.ipharm.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Medicine implements Parcelable
{
    private String nameM;
    private String laboFabM;
    private String phoneNumberLabo;
    private Double presentBottle;
    private Double initConst;
    private Double minConst;
    private Double maxConst;
    private Double priceM;
    private Double remainingPartM;
    private int stabilityM;
    private int quantityM;


    public Medicine(String nameM, String laboFabM, String phoneNumberLabo, Double presentBottle, Double initConst, Double minConst, Double maxConst, Double priceM, Double remainingPartM, int stabilityM, int quantityM) {
        this.nameM = nameM;
        this.laboFabM = laboFabM;
        this.phoneNumberLabo = phoneNumberLabo;
        this.presentBottle = presentBottle;
        this.initConst = initConst;
        this.minConst = minConst;
        this.maxConst = maxConst;
        this.priceM = priceM;
        this.remainingPartM = remainingPartM;
        this.stabilityM = stabilityM;
        this.quantityM = quantityM;
    }

    protected Medicine(Parcel in) {
        nameM = in.readString();
        laboFabM = in.readString();
        phoneNumberLabo = in.readString();
        if (in.readByte() == 0) {
            presentBottle = null;
        } else {
            presentBottle = in.readDouble();
        }
        if (in.readByte() == 0) {
            initConst = null;
        } else {
            initConst = in.readDouble();
        }
        if (in.readByte() == 0) {
            minConst = null;
        } else {
            minConst = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxConst = null;
        } else {
            maxConst = in.readDouble();
        }
        if (in.readByte() == 0) {
            priceM = null;
        } else {
            priceM = in.readDouble();
        }
        if (in.readByte() == 0) {
            remainingPartM = null;
        } else {
            remainingPartM = in.readDouble();
        }
        stabilityM = in.readInt();
        quantityM = in.readInt();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getNameM() { return nameM; }

    public void setNameM(String nameM) { this.nameM = nameM; }

    public String getLaboFabM() { return laboFabM; }

    public void setLaboFabM(String laboFabM) { this.laboFabM = laboFabM; }

    public String getPhoneNumberLabo() { return phoneNumberLabo; }

    public void setPhoneNumberLabo(String phoneNumberLabo) { this.phoneNumberLabo = phoneNumberLabo; }

    public Double getPresentBottle() { return presentBottle; }

    public void setPresentBottle(Double presentBottle) { this.presentBottle = presentBottle; }

    public Double getInitConst() { return initConst; }

    public void setInitConst(Double initConst) { this.initConst = initConst; }

    public Double getMinConst() { return minConst; }

    public void setMinConst(Double minConst) { this.minConst = minConst; }

    public Double getMaxConst() { return maxConst; }

    public void setMaxConst(Double maxConst) { this.maxConst = maxConst; }

    public Double getPriceM() { return priceM; }

    public void setPriceM(Double priceM) { this.priceM = priceM; }

    public Double getRemainingPartM() { return remainingPartM; }

    public void setRemainingPartM(Double remainingPartM) { this.remainingPartM = remainingPartM; }

    public int getStabilityM() { return stabilityM; }

    public void setStabilityM(int stabilityM) { this.stabilityM = stabilityM;}

    public int getQuantityM() { return quantityM; }

    public void setQuantityM(int quantityM) { this.quantityM = quantityM;}

    @Override
    public String toString() {
        return "Medicine{" +
                "nameM='" + nameM + '\'' +
                ", laboFabM='" + laboFabM + '\'' +
                ", phoneNumberLabo='" + phoneNumberLabo + '\'' +
                ", presentBottle=" + presentBottle +
                ", initConst=" + initConst +
                ", minConst=" + minConst +
                ", maxConst=" + maxConst +
                ", priceM=" + priceM +
                ", remainingPartM=" + remainingPartM +
                ", stabilityM=" + stabilityM +
                ", quantityM=" + quantityM +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(nameM);
        dest.writeString(laboFabM);
        dest.writeString(phoneNumberLabo);
        dest.writeDouble(presentBottle);
        dest.writeDouble(initConst);
        dest.writeDouble(minConst);
        dest.writeDouble(maxConst);
        dest.writeDouble(priceM);
        dest.writeDouble(remainingPartM);
        dest.writeInt(stabilityM);
        dest.writeInt(quantityM);
    }


}
