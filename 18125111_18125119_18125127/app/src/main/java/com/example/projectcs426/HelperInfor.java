package com.example.projectcs426;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class HelperInfor implements Parcelable {


    int ID =0 ;
    String HName;
    String Gender;
    String DOB;
    String Address;
    String Notes;
    Float Rating;
    Integer avatar=0;
    String phone;
    boolean available = false;
    LatLng latLng;

    public HelperInfor(){
        this.HName=null;
        this.Gender = null;
        this.DOB = null;
        this.Address = null;
        this.Notes = null;
        this.Rating = (float) 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHName() {
        return HName;
    }

    public void setHName(String HName) {
        this.HName = HName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    protected HelperInfor(Parcel in) {
        HName = in.readString();
        Gender = in.readString();
        DOB = in.readString();
        Address = in.readString();
        Notes = in.readString();
        if (in.readByte() == 0) {
            Rating = null;
        } else {
            Rating = in.readFloat();
        }
        if (in.readByte() == 0) {
            avatar = null;
        } else {
            avatar = in.readInt();
        }
        phone = in.readString();
        available = in.readByte() != 0;
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<HelperInfor> CREATOR = new Creator<HelperInfor>() {
        @Override
        public HelperInfor createFromParcel(Parcel in) {
            return new HelperInfor(in);
        }

        @Override
        public HelperInfor[] newArray(int size) {
            return new HelperInfor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(HName);
        parcel.writeString(Gender);
        parcel.writeString(DOB);
        parcel.writeString(Address);
        parcel.writeString(Notes);
        if (Rating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(Rating);
        }
        if (avatar == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(avatar);
        }
        parcel.writeString(phone);
        parcel.writeByte((byte) (available ? 1 : 0));
        parcel.writeParcelable(latLng, i);
    }
}
