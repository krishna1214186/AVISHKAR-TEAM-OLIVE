package com.krishna.team_olive.DiscreteFeatures;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UriContainer implements Parcelable {
    Uri uri;

    public UriContainer(Uri uri) {
        this.uri = uri;
    }

    protected UriContainer(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<UriContainer> CREATOR = new Creator<UriContainer>() {
        @Override
        public UriContainer createFromParcel(Parcel in) {
            return new UriContainer(in);
        }

        @Override
        public UriContainer[] newArray(int size) {
            return new UriContainer[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
    }
}
