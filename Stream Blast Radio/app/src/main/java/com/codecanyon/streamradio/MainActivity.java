package com.codecanyon.streamradio;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.codecanyon.radio.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import layout.FeedFragment;
import layout.LiveFragment;
import layout.PodcastFragment;

public class MainActivity extends AppCompatActivity {
    private static DataManager dataManager;
    private static TableLayout UIRadioList;
    private static ArrayList<String> userRadios = new ArrayList<String>();
    private static ViewPager viewPager;
    private static ImageView bufferingIndicator, speaker;
    private static LoadingAnimation bufferingAnimation;
    private static AudioManager audioManager;
    private static TextView radioListLocation;
    private static TextView radioListName;
    private static TextView radioTitle;
    private static boolean first = true;
    private static NotificationPanel nPanel;
    private ImageView screenChaneButton, plus;
    private boolean runOnce = true;
    private LinearLayout volumeLayout, volumeButton;
    private int volumeStore;
    private AdView adView;
    private Typeface fontRegular;
    private WebView twitter;
    private static boolean notificationWhile = true;
    private boolean active = true;
    private String last = "";
    public static String title = "";
    private static boolean exit = false;
    public static int page = 1;
    public static int pos = 0;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager pager;
    private int[] tabIcons = {
            R.drawable.ic_action_radio,
            R.drawable.ic_action_podcast,
            R.drawable.ic_action_device_dvr
    };
    private TextView tvToolbarText;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        notificationWhile = true;
        if (exit) {
            MusicPlayer.setIsWorking(false);
            notificationWhile = true;
            trackDetection();
            last = "";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
        if (notificationWhile) {
//            MainActivity.newNotification(getResources().getString(R.string.radio_name), active);
        }
    }

    public static void newNotification(String notText, boolean status) {
//        nPanel.showNotification(notText, status);
    }

    public static void radioListRefresh() {
        dataManager.createRadioListForRadioScreen(UIRadioList, userRadios, radioListName, radioListLocation);
    }

    public static void setViewPagerSwitch() {
        viewPager.setCurrentItem(0, true);
    }

    public static void startBufferingAnimation() {
        bufferingIndicator = LiveFragment.getLoadingImage();
        bufferingAnimation = new LoadingAnimation(bufferingIndicator);
        bufferingAnimation.startAnimation();
    }

    public static void stopBufferingAnimation() {
        bufferingIndicator = LiveFragment.getLoadingImage();
        bufferingAnimation.clearAnimation();
    }

    public static TextView getRadioListName() {
        return radioListName;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static TextView getRadioListLocation() {
        return radioListLocation;
    }

    public static void nextPage() {
        viewPager.setCurrentItem(1, true);
    }

    public static void play(String location) {
        try {
            exit = false;
            PhoneCallListener.message = false;
            if (first) {
                radioListLocation.setText(location);
                title = location;
                RadioList.nextOrPreviousRadioStation(1, radioListLocation, radioListName);
                if (!MusicPlayer.isStarted()) {
                    RadioList.nextOrPreviousRadioStation(0, radioListLocation, radioListName);
                }
                first = false;
            } else {
                if (MusicPlayer.isStarted()) {
                    MusicPlayer.stopMediaPlayer();
                } else if (LoadingAnimation.hasEnded()) {
                    try {
                        RadioList.nextOrPreviousRadioStation(0, radioListLocation, radioListName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        super.onCreate(savedInstanceState);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();

        tvToolbarText = (TextView) findViewById(R.id.tvToolbarText);
//        View android = inflater.inflate(R.layout.fragment_podcast, container, false);

        final View view = (View) findViewById(R.id.main_last);





//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

//
//
//
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//                pos = i2;
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                if (viewPager.getCurrentItem() == 0) {
//                    Log.d("currentTabItem", "LIVE");
//                }
//                else if (viewPager.getCurrentItem() == 1) {
//                    Log.d("currentTabItem", "PODCAST");
//                }
//                else {
//                    Log.d("currentTabItem", "BLAST FEED");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//
//
//        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pager.getCurrentItem() == 0) {
                    tvToolbarText.setText("Blast Radio");
                    view.setVisibility(View.VISIBLE);
                    Log.d("currentTabItem", "LIVE");
                }
                else if (pager.getCurrentItem() == 1) {
                    tvToolbarText.setText("Podcast");
                    view.setVisibility(View.INVISIBLE);
                    Log.d("currentTabItem", "PODCAST");
                }
                else {
                    tvToolbarText.setText("Blast Feed");
                    view.setVisibility(View.INVISIBLE);
                    Log.d("currentTabItem", "BLAST FEED");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dataManager = new DataManager(this);
        fontRegular = Typeface.createFromAsset(getAssets(), "fonts/font.otf");
//        radioTitle = (TextView) findViewById(R.id.radioTitle);
//        radioTitle.setTypeface(fontRegular);

        ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className,
                                           IBinder binder) {
                ((KillNotificationsService.KillBinder) binder).service.startService(new Intent(
                        MainActivity.this, KillNotificationsService.class));
                Intent notificationIntent = new Intent(MainActivity.this,
                        this.getClass());
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        MainActivity.this, 0, notificationIntent, 0);

                NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(MainActivity.this);
                builder.setContentTitle(getResources().getString(R.string.radio_name));
                builder.setSmallIcon(R.drawable.ic_launcher);
                builder.setContentIntent(pendingIntent);
                builder.build();

                Notification myNotication = builder.getNotification();
                mNM.notify(KillNotificationsService.NOTIFICATION_ID, myNotication);
            }

            public void onServiceDisconnected(ComponentName className) {
            }

        };
        bindService(new Intent(MainActivity.this,
                        KillNotificationsService.class), mConnection,
                Context.BIND_AUTO_CREATE);

//        plus = (ImageView) findViewById(R.id.plus);
//        plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notificationWhile = false;
//                try {
//                    MusicPlayer.stopMediaPlayer();
//                } catch (Exception e) {
//                    e.getMessage();
//                }
//                NotificationPanel.notificationCancel();
//                exit = true;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });

        TabPagerAdapter tabPageAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabPageAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                pos = i2;
            }

            @Override
            public void onPageSelected(int i) {
                if (viewPager.getCurrentItem() == 0) {
                    radioTitle.setText(getResources().getString(R.string.title_1));
                    page = 1;
                    radioTitle.setTextColor(Color.parseColor("#7B000000"));
                    screenChaneButton.setImageResource(R.drawable.switch_page);
                    adView.setVisibility(View.INVISIBLE);
                } else {
                    page = 2;
                    radioTitle.setText(getResources().getString(R.string.title_2));
                    radioTitle.setTextColor(Color.parseColor("#FF4C82C0"));
                    screenChaneButton.setImageResource(R.drawable.back);
                    if (Boolean.parseBoolean(getResources().getString(R.string.admob_true_or_false)))
                        adView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

//        screenChaneButton = (ImageView) findViewById(R.id.nextScreen);
//        screenChaneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (viewPager.getCurrentItem() == 0) viewPager.setCurrentItem(1, true);
//                else viewPager.setCurrentItem(0, true);
//            }
//        });
        speaker = (ImageView) findViewById(R.id.speaker);
        speaker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeStore, volumeStore);
                    defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
                } else {
                    volumeStore = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
                }
                return false;
            }
        });
        volumeLayout = (LinearLayout) findViewById(R.id.linearLayout_t);
        volumeButton = (LinearLayout) findViewById(R.id.button_t);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        registerReceiver(new HeadsetReceiver(getApplicationContext()), new IntentFilter(Intent.ACTION_HEADSET_PLUG));

//        adView = (AdView) this.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        adView.setVisibility(View.INVISIBLE);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneCallListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LiveFragment(), "Live");
        adapter.addFragment(new PodcastFragment(), "Podcasts");
        adapter.addFragment(new FeedFragment(), "Blast Feed");
        viewPager.setAdapter(adapter);

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//
//
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//                pos = i2;
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                if (viewPager.getCurrentItem() == 0) {
//                    radioTitle.setText(getResources().getString(R.string.title_1));
//                    page = 1;
//
//                    radioTitle.setTextColor(Color.parseColor("#7B000000"));
//                    screenChaneButton.setImageResource(R.drawable.switch_page);
////                    adView.setVisibility(View.INVISIBLE);
//                } else {
//                    page = 2;
//                    radioTitle.setText(getResources().getString(R.string.title_2));
//                    radioTitle.setTextColor(Color.parseColor("#FF4C82C0"));
//                    screenChaneButton.setImageResource(R.drawable.back);
////                    if (Boolean.parseBoolean(getResources().getString(R.string.admob_true_or_false)))
////                        adView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//
//
//        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            play(this.getResources().getString(R.string.radio_location));
        }
        else if (item.getItemId() == R.id.action_exit) {
                notificationWhile = false;
                try {
                    MusicPlayer.stopMediaPlayer();
                } catch (Exception e) {
                    e.getMessage();
                }
                NotificationPanel.notificationCancel();
                exit = true;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (runOnce) {
            UIRadioList = (TableLayout) findViewById(R.id.radioListUi);
            radioListName = (TextView) findViewById(R.id.mainRadioName);
            radioListLocation = (TextView) findViewById(R.id.mainRadioLocation);
            if (title.length() == 0) {
                radioListLocation.setText(getResources().getString(R.string.welcome_small));
            } else {
                radioListLocation.setText(title);
            }
//            startWallpaperAnimation();
            radioListRefresh();
            volumeBarReaction(volumeLayout, volumeButton, audioManager);
            connectionDialog(isOnline());
            trackDetection();
            nPanel = new NotificationPanel(this);
            nPanel.showNotification(this.getResources().getString(R.string.radio_name), true);
            twitter = (WebView) findViewById(R.id.webView);
            twitter.setWebViewClient(new WebViewClient());
            twitter.getSettings().setJavaScriptEnabled(true);
            twitter.loadUrl(getResources().getString(R.string.web_url));
            twitter.refreshDrawableState();
            runOnce = false;

            if (!MusicPlayer.isStarted()) {
                if (Boolean.parseBoolean(this.getResources().getString(R.string.autostart_true_or_false))) {
                    play(this.getResources().getString(R.string.radio_location));
                }
            }
        }
        defaultVolumeBarPosition(audioManager, volumeLayout, volumeButton);
    }
//
//    private void startWallpaperAnimation() {
//        Point size = new Point();
//        Display display = getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        LinearLayout picture = (LinearLayout) findViewById(R.id.wallpaper);
//        TranslateAnimation animation = new TranslateAnimation(0, 0 - (picture.getWidth() - size.x), 0, 0);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setDuration(200000);
//        animation.setFillAfter(true);
//        animation.setRepeatMode(Animation.REVERSE);
//        animation.setRepeatCount(Animation.INFINITE);
//        picture.startAnimation(animation);
//    }

    public void defaultVolumeBarPosition(AudioManager audioManager, LinearLayout volumeLayout, LinearLayout volumeButton) {
        float actual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float endPoint = volumeLayout.getWidth() - volumeButton.getWidth();
        volumeButton.setX((endPoint / max * actual));
        if (volumeButton.getX() == 0)
            speaker.setImageResource(R.drawable.volume_muted);
        else speaker.setImageResource(R.drawable.volume_on);
    }

    public void volumeBarReaction(final LinearLayout volumeLayout, final LinearLayout volumeButton, final AudioManager audioManager) {

        volumeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float endPoint = volumeLayout.getWidth() - volumeButton.getWidth();
                volumeButton.setX(motionEvent.getX() - volumeButton.getWidth() / 2);

                if (volumeButton.getX() >= 0) {
                    float pos = volumeButton.getX() / (endPoint / max);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) pos, 0);
                }
                if (volumeButton.getX() >= endPoint) {
                    volumeButton.setX(endPoint);
                }
                if (volumeButton.getX() <= 0) {
                    volumeButton.setX(0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    speaker.setImageResource(R.drawable.volume_muted);
                } else speaker.setImageResource(R.drawable.volume_on);
                return true;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void connectionDialog(boolean isOnline) {
        if (!isOnline) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.without_internet);
            FrameLayout mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
            dialog.getWindow().setLayout((int) (mainLayout.getWidth() * 0.8), (int) (mainLayout.getHeight() * 0.45));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button retryBtn = (Button) dialog.getWindow().findViewById(R.id.retry);
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOnline()) {
                        dialog.dismiss();
                        dialog.show();
                    } else {
                        dialog.dismiss();
                        twitter.loadUrl(getResources().getString(R.string.web_url));
                    }
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (!isOnline()) {
                        dialog.dismiss();
                        dialog.show();
                    } else
                        twitter.loadUrl(getResources().getString(R.string.web_url));
                }
            });
            dialog.show();
        }
    }

    public void trackDetection() {
        new Thread() {
            @Override
            public void run() {
                while (notificationWhile) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!PhoneCallListener.message) {
                                if (MusicPlayer.isWorking()) {
                                    if (!MusicPlayer.getTrackTitle().trim().toString().equals("")) {
                                        title = MusicPlayer.getTrackTitle().trim().toString();
                                        if (title.contains("~+") || title.contains("ad|")) {
                                            title = "COMMERICAL BREAK";
                                        }
                                        if (title.length() > 0 && title.charAt(title.length() - 1) == '-') {
                                            title = title.substring(0, title.length() - 1).trim();
                                        }

                                        String[] parts = title.split("\\(");
                                        if (parts.length == 2) {
                                            radioListLocation.setText(parts[0].toString() + "\n(" + parts[1]);
                                            title = parts[0].toString() + "\n(" + parts[1];
                                        } else
                                            radioListLocation.setText(title);
                                        if (!last.equals(title)) {
                                            MainActivity.newNotification(getResources().getString(R.string.radio_name), active);
                                            last = title;
                                        }
                                    } else if (MusicPlayer.getTrackTitle().trim().toString().equals("") && MusicPlayer.isStarted() && !radioListLocation.getText().toString().equals(getResources().getString(R.string.radio_location))) {
                                        title = getResources().getString(R.string.radio_location);
                                        radioListLocation.setText(title);
                                        MainActivity.newNotification(getResources().getString(R.string.radio_name), active);
                                    }
                                } else {
                                    if (!radioListLocation.getText().toString().equals(title) && !exit) {
                                        title = getResources().getString(R.string.radio_offline);
                                        radioListLocation.setText(title);
                                        MainActivity.newNotification(getResources().getString(R.string.radio_name), active);
                                    } else {
                                        title = getResources().getString(R.string.welcome_small);
                                        radioListLocation.setText(title);
                                        MainActivity.newNotification(getResources().getString(R.string.radio_name), active);
                                    }
                                }
                            } else {
                                radioListLocation.setText(getResources().getString(R.string.resume));
                            }
                        }
                    });
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (exit == true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }.start();
    }

    // This fires when a notification is opened by tapping on it or one is received while the app is running.
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected"))

                        Log.d("OneSignalExample", "OneSignal notification button with id " + additionalData.getString("actionSelected") + " pressed");

                    Log.d("OneSignalExample", "Full additionalData:\n" + additionalData.toString());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
    }
}


