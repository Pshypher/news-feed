package com.example.ud809_newsfeed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    private NewsFeedAdapter adapter;
    private View newsfeedlayout;


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment getInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        newsfeedlayout = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ListView listView = (ListView) newsfeedlayout.findViewById(R.id.list_view);
        adapter = new NewsFeedAdapter(getContext(), 0, new ArrayList<Feed>());
        listView.setAdapter(adapter);

        return newsfeedlayout;
    }

    public ProgressBar getSpinner() {
        return ((ProgressBar) newsfeedlayout.findViewById(R.id.loading_spinner));
    }

    public NewsFeedAdapter getAdapter() {
        return adapter;
    }
}
