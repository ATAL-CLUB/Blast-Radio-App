package com.codecanyon.streamradio;

/**
 * Created by emmanueladeleke on 14/06/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codecanyon.radio.R;

import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.MyViewHolder> {

    private List<Podcast> podcastList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public PodcastAdapter(List<Podcast> podcastList) {
        this.podcastList = podcastList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Podcast podcast = podcastList.get(position);
        holder.title.setText(podcast.getTitle());
        holder.description.setText(podcast.getDescription());
        holder.year.setText(podcast.getYear());
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }
}