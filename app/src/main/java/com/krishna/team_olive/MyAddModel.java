package com.krishna.team_olive;

import android.net.Uri;

public class MyAddModel {
    private String name;
    private String cateogary;
    private Uri uri;

    public MyAddModel(String name, String cateogary, Uri uri) {
        this.name = name;
        this.cateogary = cateogary;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCateogary() {
        return cateogary;
    }

    public void setCateogary(String cateogary) {
        this.cateogary = cateogary;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
