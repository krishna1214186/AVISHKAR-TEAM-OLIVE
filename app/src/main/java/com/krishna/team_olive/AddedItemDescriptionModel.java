package com.krishna.team_olive;

import android.os.Parcel;
import android.os.Parcelable;

public class AddedItemDescriptionModel implements Parcelable {

    private String cateogary;
    private String name;
    private String ageOfProduct;
    private String description;
    private String adress1;
    private String adress2;//landmark

    private String imageurl;
    private String pincode;
    private String typeOfExchange;
    private String exchangeCateogary;
    private String ratings;
    String extension;
    String postid;
    public AddedItemDescriptionModel()
    {}

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public AddedItemDescriptionModel(String postid, String cateogary, String name, String ageOfProduct, String description, String adress1, String adress2, String imageurl, String pincode, String typeOfExchange, String exchangeCateogary, String ratings, String extension) {

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

    public static final Creator<AddedItemDescriptionModel> CREATOR = new Creator<AddedItemDescriptionModel>() {
        @Override
        public AddedItemDescriptionModel createFromParcel(Parcel in) {
            return new AddedItemDescriptionModel(in);
        }

        @Override
        public AddedItemDescriptionModel[] newArray(int size) {
            return new AddedItemDescriptionModel[size];
        }
    };


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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ratings);

        dest.writeString(cateogary);
        dest.writeString(name);
        dest.writeString(ageOfProduct);
        dest.writeString(description);
        dest.writeString(adress1);
        dest.writeString(adress2);
        dest.writeString(imageurl);
        dest.writeString(pincode);
        dest.writeString(typeOfExchange);
        dest.writeString(exchangeCateogary);

    }
}


