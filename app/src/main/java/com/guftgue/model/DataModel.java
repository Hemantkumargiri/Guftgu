package com.guftgue.model;



public class DataModel {

    public String mTvEncryption;
    public String mTvUserName;
    public int mImgProfile;
    public String name;
    public String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getEncription_type() {
        return encription_type;
    }

    public void setEncription_type(String encription_type) {
        this.encription_type = encription_type;
    }

    public String encription_type;
    public String encription_name;

    public String getEncription_name() {
        return encription_name;
    }

    public void setEncription_name(String encription_name) {
        this.encription_name = encription_name;
    }

    public DataModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataModel(String mTvUserName, int mImgProfile) {
        this.mTvUserName = mTvUserName;
        this.mImgProfile = mImgProfile;
    }

    public DataModel(String mTvEncryption) {
        this.mTvEncryption = mTvEncryption;
    }
}
