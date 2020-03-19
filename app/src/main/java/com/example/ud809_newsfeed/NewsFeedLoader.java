package com.example.ud809_newsfeed;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;


public class NewsFeedLoader extends AsyncTaskLoader<List<Feed>> {

    private List<Feed> resultFromHttp;
    private String urlString;

    private static final String LOG_TAG = NewsFeedLoader.class.getSimpleName();

    public NewsFeedLoader(@NonNull Context context, String urlString) {
        super(context);
        this.urlString = urlString;
        resultFromHttp = null;
    }

    @Nullable
    @Override
    public List<Feed> loadInBackground() {

        List<Feed> newsFeeds = null;
        try {
            newsFeeds = NetworkUtil.fetchData(urlString);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem releasing IO stream", e);
        }
        return newsFeeds;
    }

    @Override
    protected void onStartLoading() {

        if (resultFromHttp != null) {
            deliverResult(resultFromHttp);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable List<Feed> data) {
        super.deliverResult(data);
        resultFromHttp = data;
    }
}
