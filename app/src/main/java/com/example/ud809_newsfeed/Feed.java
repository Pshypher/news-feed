package com.example.ud809_newsfeed;

import android.graphics.Bitmap;

public class Feed {

    private Bitmap mThumbnail;
    private String mSection;
    private String mWebTitle;
    private String mContributor;
    private String mPublicationDate;
    private String mWebURL;

    public Feed(Bitmap thumbnail, String section, String webTitle, String contributor,
                String publicationDate, String url) {
        mThumbnail = thumbnail;
        mSection = section;
        mWebTitle = webTitle;
        mContributor = contributor;
        mPublicationDate = publicationDate;
        mWebURL = url;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public String getSection() {
        return mSection;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getContributor() {
        return mContributor;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getWebURL() {
        return mWebURL;
    }
}
