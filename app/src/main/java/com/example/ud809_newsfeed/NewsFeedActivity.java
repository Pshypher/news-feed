package com.example.ud809_newsfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class NewsFeedActivity extends FragmentActivity implements
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
        setUpDefaultDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setUpDefaultDisplay();
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

    private void setUpDefaultDisplay() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        MenuItem checkedItem = navigationView.getCheckedItem();
        setTitle(checkedItem.getTitle());

        // add this line to display all news feeds when the activity is loaded
        onNavigationItemSelected(checkedItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        switch (item.getItemId()) {
            case R.id.international:
                id = 0;
                break;
            case R.id.opinion:
                id = 1;
                break;
            case R.id.sport:
                id = 2;
                break;
            case R.id.culture:
                id = 3;
                break;
            case R.id.life_style:
                id = 4;
                break;
            default:
                id = 5;
        }

        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_SECTION_SEGMENT, getSection(id));
        AsyncTaskLoader loader =
                (AsyncTaskLoader) getSupportLoaderManager().getLoader(id);
        if (loader == null && !isConnected()) {
            newsFeedFragment.hideSpinner();
            newsFeedFragment.setEmptyViewText(getString(R.string.bad_connection));
        } else {
            newsFeedFragment.clear();
            newsFeedFragment.revealSpinner();
            newsFeedFragment.setEmptyViewText(null);
            getSupportLoaderManager().initLoader(id, queryBundle, this);
        }
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    private String getSection(int id) {
        String section;
        switch (id) {
            case 0:
                section = ALL_FEEDS;
                break;
            case 1:
                section = OPINION;
                break;
            case 2:
                section = SPORTS;
                break;
            case 3:
                section = CULTURE;
                break;
            case 4:
                section = LIFE_STYLE;
                break;
            default:
                section = NO_FEED;
        }

        return section;
    }

    @NonNull
    @Override
    public Loader<List<Feed>> onCreateLoader(int id, @Nullable Bundle args) {
        final String API_KEY = "test";
        final String SEGMENT = "search";
        final String QUERY_FIELDS = "headline,thumbnail";
        final String QUERY_TAGS = "contributor";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String limitString = sharedPreferences.getString(
                getString(R.string.settings_feed_limit_key),
                getString(R.string.settings_feed_limit_default)
                );
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        int limit = Integer.parseInt(limitString);
        if (limit <= 0) {
            limit = 1;
        } else if (limit > 50) {
            limit = 50;
        }

        limitString = String.valueOf(limit);

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendPath(SEGMENT);
        builder.appendQueryParameter("section", args.getString(QUERY_SECTION_SEGMENT));
        builder.appendQueryParameter("show-fields", QUERY_FIELDS);
        builder.appendQueryParameter("show-tags", QUERY_TAGS);
        builder.appendQueryParameter("api-key", API_KEY);
        builder.appendQueryParameter("page-size", limitString);
        builder.appendQueryParameter("order-by", orderBy);

        return new NewsFeedLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Feed>> loader, List<Feed> feeds) {
        newsFeedFragment.hideSpinner();
        newsFeedFragment.clear();
        if (feeds != null) {
            newsFeedFragment.addAll(feeds);
        } else {
            newsFeedFragment.setEmptyViewText(getString(R.string.no_results));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Feed>> loader) {
        newsFeedFragment.clear();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
