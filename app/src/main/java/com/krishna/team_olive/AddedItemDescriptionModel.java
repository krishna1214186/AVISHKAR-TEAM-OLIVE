package com.krishna.team_olive;

public class AddedItemDescriptionModel {
    String cateogary;
    String name;
    String ageOfProduct;
    String description;
    String adress1;
    String adress2;
    String imageurl;
    String pincode;
    public AddedItemDescriptionModel()
    {}

    public AddedItemDescriptionModel(String cateogary, String name, String ageOfProduct, String description, String adress1, String adress2, String imageurl, String pincode) {
        this.cateogary = cateogary;
        this.name = name;
        this.ageOfProduct = ageOfProduct;
        this.description = description;
        this.adress1 = adress1;
        this.adress2 = adress2;
        this.imageurl = imageurl;
        this.pincode = pincode;
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
