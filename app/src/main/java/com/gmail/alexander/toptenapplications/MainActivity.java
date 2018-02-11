package com.gmail.alexander.toptenapplications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.gmail.alexander.toptenapplications.incomedata.DownloadData;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 *         <alexandervladimirov1902@gmail.com>
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedsURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    private String feedCachedUrl = "INVALIDED";
    public static final String STATE_UTL = "feedUTL";
    public static final String STATE_LIMIT = "feedLimit";

    /**
     * Starting point of the application.
     *
     * @param savedInstanceState is used to retrieve saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);
        if (savedInstanceState != null) {
            feedsURL = (String) savedInstanceState.getSerializable(STATE_UTL);
            feedLimit = (int) savedInstanceState.getSerializable(STATE_LIMIT);
        }

        downloadUrl(String.format(feedsURL, feedLimit));
    }

    /**
     * Logic that creates the menu.
     *
     * @param menu that is going to be displayed to the user.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if (feedLimit == 10) {
            menu.findItem(R.id.menuTopTen).setChecked(true);
        } else {
            menu.findItem(R.id.menuTop25).setChecked(true);
        }
        return true;
    }

    /**
     * This is the item menu choose method.
     *
     * @param item picked item from the menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuFree:
                feedsURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.menuPaidApps:
                feedsURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.menuSongs:
                feedsURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.menuTopTen:
            case R.id.menuTop25:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " Setting feedLimit to " + feedLimit);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " feedLimit unchanged");
                }
                break;
            case R.id.menuRefresh:
                feedCachedUrl = "INVALIDATED";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(String.format(feedsURL, feedLimit));
        return true;

    }

    /**
     * Saves Feeds url and feed limit.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_UTL, feedsURL);
        outState.putSerializable(STATE_LIMIT, feedLimit);
        super.onSaveInstanceState(outState);
    }

    /**
     * Download data from given url.
     *
     * @param url
     */
    private void downloadUrl(String url) {
        if (!url.equals(feedCachedUrl)) {
            DownloadData downloadData = new DownloadData(MainActivity.this, R.layout.list_item, listApps);
            downloadData.execute(url);
            feedCachedUrl = url;

        }
    }
}
