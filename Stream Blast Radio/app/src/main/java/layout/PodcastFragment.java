package layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codecanyon.radio.R;
import com.codecanyon.streamradio.Podcast;
import com.codecanyon.streamradio.PodcastAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 */
public class PodcastFragment extends Fragment {

    private List<Podcast> podcastList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PodcastAdapter mAdapter;

    public PodcastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View android = inflater.inflate(R.layout.fragment_podcast, container, false);

        recyclerView = (RecyclerView) android.findViewById(R.id.recycler_view);

        mAdapter = new PodcastAdapter(podcastList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        preparePodcastDummyData();
        return android;
    }

    private void preparePodcastDummyData() {
        Podcast podcast = new Podcast("Test Podcast", "N/A", "2002", 40, 30);
        podcastList.add(podcast);
//
        podcast = new Podcast("Sean and Ben.. Again", "W1?!", "2002", 434, 10);
        podcastList.add(podcast);

//        podcast = new Podcast("EU.. In or out?", "200", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Logic vs Fruity", "Annoying talks about diffrent DAWs", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Blast Votes", "Voting your best DJ of 2015", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Class of '94", "A podcast with '94 TVU graduates", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("About NewBaseMusic", "Introducting a new band NBM", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("God bless England", "Discussing the climate in England", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Star Trek", "Science Fiction", "2009");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("The LEGO Podcast", "Animation", "2014");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Where are they now?", "New podcast by Daniel & Craig", "2015");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Jazz Session", "New Jazz music", "2014");
//        podcastList.add(podcast);
//
//        podcast = new Podcast("Blast Radio Podcast", "Introducing our new podcast", "2014");
//        podcastList.add(podcast);

        podcastList.add(podcast);

        mAdapter.notifyDataSetChanged();
    }

    private class PodcastTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        private Object getJsonData() {

            return null;
        }
    }


}
