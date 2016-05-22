package com.codecanyon.streamradio;

import android.content.Context;
import android.media.AudioTrack;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;

public class MusicPlayer {
    private static boolean isStarted = false;
    private static MultiPlayer multiPlayer;
    private static String trackTitle = "";
    private static String radioName = "";
    private Context context;
    private MusicPlayerTask musicPlayerTask = new MusicPlayerTask();
    private ConnectivityManager cm;
    private NetworkInfo netInfo;
    private RadioListElement radioListElement;

    public static boolean isWorking() {
        return isWorking;
    }

    public static void setIsWorking(boolean isWorking) {
        MusicPlayer.isWorking = isWorking;
    }

    private static boolean isWorking = true;

    public static String getRadioName() {
        return radioName;
    }

    public static String getTrackTitle() {
        return trackTitle;
    }

    public static boolean isStarted() {
        return isStarted;
    }

    public static void stopMediaPlayer() {
        isStarted = false;
        multiPlayer.stop();
    }

    public void play(RadioListElement rle) {
        isWorking = true;
        radioListElement = rle;
        musicPlayerTask.cancel(true);
        musicPlayerTask = new MusicPlayerTask();
        radioListElement.getName();
        radioName = radioListElement.getName();
        musicPlayerTask.execute(radioListElement.getUrl());
        context = radioListElement.getContext();
    }

    public class MusicPlayerTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isWorking = true;
            MainActivity.setViewPagerSwitch();
            MainActivity.startBufferingAnimation();
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                multiPlayer.stop();
            } catch (Exception e) {
                e.getMessage();
            }
            MainActivity.newNotification(MusicPlayer.getRadioName(), true);
            multiPlayer = new MultiPlayer(new PlayerCallback() {

                @Override
                public void playerStopped(int arg0) {
                    isStarted = false;
                }

                @Override
                public void playerStarted() {
                    try {
                        MainActivity.stopBufferingAnimation();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    isStarted = true;
                }

                @Override
                public void playerPCMFeedBuffer(boolean arg0, int arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void playerMetadata(String arg0, String arg1) {
                    // TODO Auto-generated method stub
                    if ("StreamTitle".equals(arg0)) {
                        trackTitle = arg1.toString();
                    }
                }

                @Override
                public void playerException(Throwable arg0) {
                    // TODO Auto-generated method stub
                    isWorking = false;
                    try {
                        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            MainActivity.stopBufferingAnimation();
                            isWorking = false;

                        } else {
                            MainActivity.stopBufferingAnimation();
                            isWorking = false;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }

                @Override
                public void playerAudioTrackCreated(AudioTrack arg0) {
                    // TODO Auto-generated method stub

                }
            }, 750, 700);
            multiPlayer.playAsync(params[0]);

            try {
                java.net.URL.setURLStreamHandlerFactory(new java.net.URLStreamHandlerFactory() {
                    public java.net.URLStreamHandler createURLStreamHandler(String protocol) {
                        if ("icy".equals(protocol))
                            return new com.spoledge.aacdecoder.IcyURLStreamHandler();
                        return null;
                    }
                });
            } catch (Throwable t) {
            }
            return null;
        }
    }
}
