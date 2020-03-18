package com.example.ud809_newsfeed;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NewsFeedPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private static final String ALL_FEEDS = "world";
    private static final String OPINION = "commentisfree";
    private static final String SPORTS = "sport";
    private static final String CULTURE = "culture";
    private static final String LIFE_STYLE = "lifeandstyle";

    private NewsFeedPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    private void setPagerAdapterContext(Context ctxt) {
        context = ctxt;
    }

    public static NewsFeedPagerAdapter makeNewsFeedPagerAdapter(
            FragmentManager fm, Context ctxt) {
        NewsFeedPagerAdapter adapter = new NewsFeedPagerAdapter(fm);
        adapter.setPagerAdapterContext(ctxt);
        return adapter;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        NewsFeedFragment fragment;

        switch (position) {
            case 0:
                fragment = NewsFeedFragment.getInstance(position, ALL_FEEDS);
                break;
            case 1:
                fragment = NewsFeedFragment.getInstance(position, OPINION);
                break;
            case 2:
                fragment = NewsFeedFragment.getInstance(position, SPORTS);
                break;
            case 3:
                fragment = NewsFeedFragment.getInstance(position, CULTURE);
                break;
            case 4:
                fragment = NewsFeedFragment.getInstance(position, LIFE_STYLE);
                break;
            default:
                fragment = null;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        final int SECTIONS = 5;
        return SECTIONS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title;
        switch (position) {
            case 0:
                title = context.getString(R.string.world_news);
                break;
            case 1:
                title = context.getString(R.string.comment_is_free);
                break;
            case 2:
                title = context.getString(R.string.sport);
                break;
            case 3:
                title = context.getString(R.string.culture);
                break;
            case 4:
                title = context.getString(R.string.life_and_style);
                break;
            default:
                title = null;
        }

        return title;
    }
}
