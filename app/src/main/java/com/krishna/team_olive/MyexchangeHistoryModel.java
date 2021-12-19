package com.krishna.team_olive;

import android.net.Uri;

public class MyexchangeHistoryModel {
    Uri  uri;
    String name;
    String cateogaryOfProduct;
    String cayteogaryExchanged;

    public MyexchangeHistoryModel(Uri uri, String name, String cateogaryOfProduct, String cayteogaryExchanged) {
        this.uri = uri;
        this.name = name;
        this.cateogaryOfProduct = cateogaryOfProduct;
        this.cayteogaryExchanged = cayteogaryExchanged;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCateogaryOfProduct() {
        return cateogaryOfProduct;
    }

    public void setCateogaryOfProduct(String cateogaryOfProduct) {
        this.cateogaryOfProduct = cateogaryOfProduct;
    }

    public String getCayteogaryExchanged() {
        return cayteogaryExchanged;
    }

    public void setCayteogaryExchanged(String cayteogaryExchanged) {
        this.cayteogaryExchanged = cayteogaryExchanged;
    }
}
