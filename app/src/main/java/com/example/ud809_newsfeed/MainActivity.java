package com.example.ud809_newsfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends FragmentActivity implements
        NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<Feed>> {


    private NewsFeedFragment newsFeedFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private static final String ALL_FEEDS = "world";
    private static final String OPINION = "commentisfree";
    private static final String SPORTS = "sport";
    private static final String CULTURE = "culture";
    private static final String LIFE_STYLE = "lifeandstyle";
    private static final String NO_FEED = null;

    private static final String QUERY_SECTION_SEGMENT = "section";
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsFeedFragment = (NewsFeedFragment) getSupportFragmentManager().getFragments().get(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        drawer.addDrawerListener(actionBarDrawerToggle);
        build();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        build();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void build() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        MenuItem checkedItem = navigationView.getCheckedItem();
        setTitle(checkedItem.getTitle());

        // add this line to display all news feeds when the activity is loaded
        onNavigationItemSelected(checkedItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        String section;
        switch (item.getItemId()) {
            case R.id.international:
                id = 0;
                section = ALL_FEEDS;
                break;
            case R.id.opinion:
                id = 1;
                section = OPINION;
                break;
            case R.id.sport:
                id = 2;
                section = SPORTS;
                break;
            case R.id.culture:
                id = 3;
                section = CULTURE;
                break;
            case R.id.life_style:
                id = 4;
                section = LIFE_STYLE;
                break;
            default:
                id = 5;
                section = NO_FEED;
        }

        ProgressBar progressBar = newsFeedFragment.getSpinner();
        NewsFeedAdapter adapter = newsFeedFragment.getAdapter();
        progressBar.setVisibility(View.VISIBLE);
        adapter.clear();

        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_SECTION_SEGMENT, section);
        getSupportLoaderManager().initLoader(id, queryBundle, this);
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
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
        builder.appendQueryParameter("q", args.getString(QUERY_SECTION_SEGMENT));
        builder.appendQueryParameter("show-fields", QUERY_FIELDS);
        builder.appendQueryParameter("show-tags", QUERY_TAGS);
        builder.appendQueryParameter("api-key", GUARDIAN_API_KEY);

        return new NewsFeedLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Feed>> loader, List<Feed> feeds) {
        newsFeedFragment.getSpinner().setVisibility(View.GONE);
        newsFeedFragment.getAdapter().clear();
        newsFeedFragment.getAdapter().addAll(feeds);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Feed>> loader) {
        newsFeedFragment.getAdapter().clear();
    }
}
