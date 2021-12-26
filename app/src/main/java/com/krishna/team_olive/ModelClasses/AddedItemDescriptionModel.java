package com.krishna.team_olive.ModelClasses;

import android.os.Parcel;

import java.io.Serializable;

//Mode class for adding post/items for exchange and donation.

public class AddedItemDescriptionModel implements Serializable {

    private String cateogary;   //Category of item selected
    private String name;        //Name of item
    private String ageOfProduct;//Age of item
    private String description; //Description of product
    private String adress1;     //Initial adress of product
    private String adress2;     //Landmark/City of product
    private String imageurl;    //Url for thumnail of image
    private String pincode;     //Pincode of location where product is present
    private String typeOfExchange;//whether the exchange is donation type or exchange type
    private String exchangeCateogary;//category of demanded product in return for exchange
    private String ratings;     //Ratings of user
    String extension;           //Extension of thumnail image
    String postid;              //Id when image is uploaded to firebase
    private String uid;         //Uid of user uploading the image
    public AddedItemDescriptionModel()
    {}

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public AddedItemDescriptionModel(String postid, String cateogary, String name, String ageOfProduct, String description, String adress1, String adress2, String imageurl, String pincode, String typeOfExchange, String exchangeCateogary, String ratings, String extension,String uid) {

        this.postid = postid;
        this.cateogary = cateogary;
        this.name = name;
        this.ageOfProduct = ageOfProduct;
        this.description = description;
        this.adress1 = adress1;
        this.adress2 = adress2;
        this.imageurl = imageurl;
        this.pincode = pincode;
        this.typeOfExchange=typeOfExchange;
        this.exchangeCateogary=exchangeCateogary;
        this.ratings=ratings;
        this.extension=extension;
        this.uid = uid;

    }

    protected AddedItemDescriptionModel(Parcel in) {
        cateogary = in.readString();
        name = in.readString();
        ageOfProduct = in.readString();
        description = in.readString();
        adress1 = in.readString();
        adress2 = in.readString();
        imageurl = in.readString();
        pincode = in.readString();
        typeOfExchange=in.readString();
        exchangeCateogary=in.readString();
        ratings = in.readString();
        extension=in.readString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTypeOfExchange() {
        return typeOfExchange;
    }

    public void setTypeOfExchange(String typeOfExchange) {
        this.typeOfExchange = typeOfExchange;
    }

    public String getExchangeCateogary() {
        return exchangeCateogary;
    }

    public void setExchangeCateogary(String exchangeCateogary) {
        this.exchangeCateogary = exchangeCateogary;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getCateogary() {
        return cateogary;
    }

    public void setCateogary(String cateogary) {
        this.cateogary = cateogary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeOfProduct() {
        return ageOfProduct;
    }

    public void setAgeOfProduct(String ageOfProduct) {
        this.ageOfProduct = ageOfProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress1() {
        return adress1;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

}