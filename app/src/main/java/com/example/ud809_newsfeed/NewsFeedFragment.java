package com.example.ud809_newsfeed;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Feed>> {

    private NewsFeedAdapter mAdapter;
    private View mNewsFeedLayout;

    private static final String URL_SECTION_ID = "id";
    private static final String QUERY_SECTION = "section";
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com";

    public static final String REQUEST_URL_STRING = "url_string";

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment getInstance(int id, String section) {
        Bundle args = new Bundle();
        args.putInt(URL_SECTION_ID, id);
        args.putString(QUERY_SECTION, section);
        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get previous instance state and initialize loader
        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_SECTION, getArguments().getString(QUERY_SECTION));
        int id = getArguments().getInt(URL_SECTION_ID);
        getLoaderManager().initLoader(id, queryBundle, this);
        // Inflate the layout for this fragment
        mNewsFeedLayout = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ListView listView = (ListView) mNewsFeedLayout.findViewById(R.id.list_view);
        mAdapter = new NewsFeedAdapter(getContext(), 0, new ArrayList<Feed>());
        listView.setAdapter(mAdapter);

        return mNewsFeedLayout;
    }

    @NonNull
    @Override
    public Loader<List<Feed>> onCreateLoader(int id, @Nullable Bundle args) {
        final String GUARDIAN_API_KEY = "e45493c8-d0f7-494d-aa0a-01d9e4fdd9b8";
        final String SEGMENT = "search";
        final String QUERY_FIELDS = "headline,thumbnail";
        final String QUERY_TAGS = "contributor";

        Uri baseUrl = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder builder = baseUrl.buildUpon();
        builder.appendPath(SEGMENT);
        builder.appendQueryParameter("q", args.getString(QUERY_SECTION));
        builder.appendQueryParameter("show-fields", QUERY_FIELDS);
        builder.appendQueryParameter("show-tags", QUERY_TAGS);
        builder.appendQueryParameter("api-key", GUARDIAN_API_KEY);
        args.putString(REQUEST_URL_STRING, builder.toString());

        return new NewsFeedLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Feed>> loader, List<Feed> feeds) {
        ProgressBar progressBar = (ProgressBar) mNewsFeedLayout.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();
        mAdapter.addAll(feeds);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Feed>> loader) {
        mAdapter.clear();
    }
}
