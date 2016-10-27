package layout;


import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codecanyon.radio.R;
import com.codecanyon.streamradio.MainActivity;
import com.codecanyon.streamradio.MusicPlayer;
import com.codecanyon.streamradio.OnSwipeTouchListener;
import com.codecanyon.streamradio.UntouchableHorizontalScrollView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {


    private static ImageView loading;
    private LinearLayout share;
    private TextView radioListLocation, radioListName;
    private LinearLayout linearLayout;
    private ImageView playButton;
    private int play = 1;

    public static ImageView getLoadingImage() {
        return loading;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.main_main, container, false);
        loading = (ImageView) android.findViewById(R.id.loading);
        radioListLocation = (TextView) android.findViewById(R.id.mainRadioName);
        playButton = (ImageView) android.findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play == 1) {
                    play = 0;
                    playButton.setImageResource(R.drawable.clean_pause_buttons);
                }
                else {
                    playButton.setImageResource(R.drawable.clean_play_buttons);
                    play = 1;
                }
                MainActivity.play(getResources().getString(R.string.radio_location));
            }
        });

        linearLayout = (LinearLayout) android.findViewById(R.id.wallpaper);
        radioListName = (TextView) android.findViewById(R.id.mainRadioLocation);

        Typeface fontRegular = Typeface.createFromAsset(this.getActivity().getAssets(), "fonts/font.otf");
        radioListLocation.setTypeface(fontRegular);
        radioListName.setTypeface(fontRegular);
        share = (LinearLayout) android.findViewById(R.id.shareImage);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_url));
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
        if (MusicPlayer.isStarted()) {
            radioListLocation.setText(getString(R.string.radio_name));
        }
        return android;
    }


//    public LiveFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_live, container, false);
//    }



}
