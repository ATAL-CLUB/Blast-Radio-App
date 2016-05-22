package com.codecanyon.streamradio;

import com.codecanyon.radio.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RadioListElement extends LinearLayout {

    boolean userDef;
    private boolean isPlayBol = false;
    private TextView name, frequency;
    private ImageView isPlay;
    private String url;

    public RadioListElement(Context context, String radioListName, String radioListLocation, String url) {
        super(context);
        View.inflate(context, R.layout.list_item, this);
        this.isPlay = (ImageView) findViewById(R.id.isPlay);
        this.frequency = (TextView) findViewById(R.id.radioListLocation);
        this.frequency.setText(radioListLocation);
        this.name = (TextView) findViewById(R.id.radioListName);
        this.name.setText(radioListName);
        this.url = url;
    }

    public RadioListElement(Context context, String radioListName, String radioListLocation, String url, boolean userDef) {
        super(context);
        View.inflate(context, R.layout.list_item, this);
        this.isPlay = (ImageView) findViewById(R.id.isPlay);
        this.frequency = (TextView) findViewById(R.id.radioListLocation);
        this.frequency.setText(radioListLocation);
        this.name = (TextView) findViewById(R.id.radioListName);
        this.name.setText(radioListName);
        this.url = url;
        this.userDef = userDef;
    }

    public void touchDown() {
        name.setTextColor(Color.WHITE);
    }

    public void touchUP() {
        name.setTextColor(Color.WHITE);
        isPlay.setVisibility(VISIBLE);
        isPlayBol = true;
    }

    public String getUrl() {
        return url;
    }

    public String getFrequency() {
        return frequency.getText().toString();
    }

    public String getName() {
        return name.getText().toString();
    }

    public void touchCancel() {
        name.setTextColor(Color.parseColor("#92798f"));
    }

    public void setElementDefault() {
        name.setTextColor(Color.parseColor("#92798f"));
        isPlay.setVisibility(INVISIBLE);
        isPlayBol = false;
    }

    public boolean isPlayBol() {
        return isPlayBol;
    }
}
