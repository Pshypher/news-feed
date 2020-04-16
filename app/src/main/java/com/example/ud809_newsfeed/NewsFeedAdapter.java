package com.example.ud809_newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter<Feed> {

    public NewsFeedAdapter(@NonNull Context context, int resource, @NonNull List<Feed> feeds) {
        super(context, resource, feeds);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Feed feed = getItem(position);
        View itemView = convertView;

        ViewHolder holder;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,
                    parent, false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        holder.bind(feed);
        return itemView;
    }

    private static class ViewHolder {

        private ImageView thumbnail;
        private TextView section;
        private TextView headline;
        private TextView contributor;
        private TextView date;
        private TextView time;

        public ViewHolder(View view) {
            thumbnail = (ImageView) view.findViewById(R.id.news_feed_image);
            section = (TextView) view.findViewById(R.id.news_feed_section);
            headline = (TextView) view.findViewById(R.id.headline);
            contributor = (TextView) view.findViewById(R.id.author);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
        }

        public void bind(Feed feed) {
            thumbnail.setImageBitmap(feed.getThumbnail());
            section.setText("#" + feed.getSection());
            headline.setText(feed.getWebTitle());
            contributor.setText(feed.getContributor());
            date.setText(getDate(feed.getPublicationDate()));
            time.setText(getTime(feed.getPublicationDate()));
        }

        private CharSequence getTime(String publicationDate) {
            int beginIndex = publicationDate.indexOf('T') + 1;
            String[] time = publicationDate.substring(beginIndex).split(":");
            if (time.length >= 2) {
                String hour = time[0];
                String min = time[1];
                return hour + ":" + min;
            }

            return null;
        }

        private CharSequence getDate(String publicationDate) {
            int endIndex = publicationDate.indexOf('T');
            return publicationDate.substring(0, endIndex);
        }
    }
}
