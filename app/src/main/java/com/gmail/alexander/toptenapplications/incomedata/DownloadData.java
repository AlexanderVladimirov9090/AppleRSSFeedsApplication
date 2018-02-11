package com.gmail.alexander.toptenapplications.incomedata;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.gmail.alexander.toptenapplications.MainActivity;
import com.gmail.alexander.toptenapplications.R;
import com.gmail.alexander.toptenapplications.adapters.FeedAdapter;
import com.gmail.alexander.toptenapplications.parsers.FeedParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 *         <alexandervladimirov1902@gmail.com>
 * This class is used to download the data from resource.
 */

public class DownloadData extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadData";
    private Context context;
    private int listItemId;
    private ListView listApps;
    public DownloadData(MainActivity mainActivity, int list_item, ListView listApps) {
        context = mainActivity.getApplicationContext();
        listItemId = list_item;
      this.listApps = listApps;
    }

    /**
     * When the download finishes parser and adapter are called to generate the content to user.
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter is:" + s);
        FeedParser feedParser = new FeedParser();
        feedParser.parse(s);
        FeedAdapter feedAdapter = new FeedAdapter(context, R.layout.list_record, feedParser.getFeedEntries());
        listApps.setAdapter(feedAdapter);
    }

    /**
     * Download data in the background.
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: Starts with: " + strings[0]);
        String rssFeed = downloadXML(strings[0]);
        if (rssFeed == null) {
            Log.e(TAG, "doInBackground: Error Downloading");
        }
        return rssFeed;
    }

    /**
     * Reads data from the resource and returns it as a string.
     * @param urlPath
     * @return
     */
    private String downloadXML(String urlPath) {
        StringBuilder xmlResult = new StringBuilder();
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int response = httpURLConnection.getResponseCode();
            Log.d(TAG, "downloadXML: Response code was: "+ response);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            int charsRead;
            char[] inputBuffer = new char[500];
            while (true) {
                charsRead = bufferedReader.read(inputBuffer);
                if (charsRead < 0) {
                    break;
                }
                if (charsRead > 0) {
                    xmlResult.append(String.valueOf(inputBuffer, 0, charsRead));
                }
            }
            bufferedReader.close();
            return xmlResult.toString();
        } catch (IOException e) {
            Log.e(TAG, "downloadXML: Invalid URL: " + e.getMessage());

        }
        return null;
    }


}
