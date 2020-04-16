package com.example.ud809_newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    private NewsFeedAdapter adapter;
    private View newsfeedlayout;
    private ListView.OnItemClickListener mOnItemClickListener =
            new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Feed feed = (Feed) parent.getItemAtPosition(position);
                    String webUrl = feed.getWebURL();
                    Uri webpage = Uri.parse(webUrl);
                    Intent newsArticleIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (newsArticleIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(newsArticleIntent);
                    }
                }
            };

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
        listView.setOnItemClickListener(mOnItemClickListener);
        TextView emptyStateTextView = (TextView) newsfeedlayout.findViewById(R.id.empty_field);
        listView.setEmptyView(emptyStateTextView);

        return newsfeedlayout;
    }

    public void hideSpinner() {
       ProgressBar progressBar = (ProgressBar) newsfeedlayout.findViewById(R.id.loading_spinner);
       progressBar.setVisibility(View.GONE);
    }

    public void revealSpinner() {
        ProgressBar progressBar = (ProgressBar) newsfeedlayout.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void addAll(List<Feed> feeds) {
        adapter.addAll(feeds);
    }

    public void clear() { adapter.clear(); }

    public void setEmptyViewText(String text) {
        TextView emptyStateTextView = (TextView) newsfeedlayout.findViewById(R.id.empty_field);
        emptyStateTextView.setText(text);
    }
}
