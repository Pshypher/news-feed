package com.example.ud809_newsfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtil {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private NetworkUtil() {
        // Cannot create instance of an utility class
    }

    public static List<Feed> fetchData(String urlString) throws IOException {
        URL url = createURL(urlString);

        String response = null;
        if (url != null) {
            response = makeHttpRequest(url);
        }

        List<Feed> feeds = null;
        if (!TextUtils.isEmpty(response)) {
            feeds = extractNewsFeed(response);
        }

        return feeds;
    }

    public static URL createURL(String urlString) {
        urlString = urlString.replace("http://", "https://");

        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem parsing url string", e);
        }

        return null;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            //connection.setDoInput(true);
            inputStream = connection.getInputStream();
            String response = readStream(inputStream);
            return response;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with HTTP request", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;

    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                Charset.forName("UTF-8")));

        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }

        return builder.toString();
    }

    private static Bitmap downloadImage(URL url) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem connecting to bitmap host");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return null;
    }

    private static List<Feed> extractNewsFeed(String responseString) throws IOException {
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        try {
            JSONObject root = new JSONObject(responseString);
            JSONObject responseJSON = root.getJSONObject("response");
            JSONArray results = responseJSON.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject resultFeed = results.getJSONObject(i);
                String title = resultFeed.getString("webTitle");
                String url = resultFeed.getString("webUrl");
                String date = resultFeed.getString("webPublicationDate");
                JSONObject fields = resultFeed.optJSONObject("fields");
                String thumbnailURL = fields.getString("thumbnail");
                JSONArray tags = resultFeed.optJSONArray("tags");
                String contributor = null;
                if (tags.length() > 0) {
                    JSONObject singleItemTag = tags.optJSONObject(0);
                    contributor = singleItemTag.optString("webTitle");
                }

                Bitmap image = downloadImage(createURL(thumbnailURL));
                feeds.add(new Feed(image, title, contributor, date, url));
            }

            return feeds;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
