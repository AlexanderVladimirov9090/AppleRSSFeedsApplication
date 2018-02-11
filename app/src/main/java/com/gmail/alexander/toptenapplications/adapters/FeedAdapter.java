package com.gmail.alexander.toptenapplications.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.alexander.toptenapplications.R;
import com.gmail.alexander.toptenapplications.models.FeedEntry;

import java.util.List;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 *         <alexandervladimirov1902@gmail.com>
 *             Adapter patern to connect instance from FeedEntry class to the UI.
 */

public class FeedAdapter extends ArrayAdapter{
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;


    public FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    /**
     * Count`s Feeds.
     * @return
     */
    @Override
    public int getCount() {
        return applications.size();
    }

    /**
     * Sets content to the Views
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(layoutResource,parent,false);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvArtist = (TextView) view.findViewById(R.id.tvArtist);
        TextView tvSummary = (TextView) view.findViewById(R.id.tvSummary);
        FeedEntry currentApp = applications.get(position);

        tvName.setText(currentApp.getName());
        tvArtist.setText(currentApp.getArtist());
        tvSummary.setText(currentApp.getSummary());
        return view;
    }
}
