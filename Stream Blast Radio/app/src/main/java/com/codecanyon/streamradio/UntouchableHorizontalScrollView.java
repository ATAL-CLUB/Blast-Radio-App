package com.codecanyon.streamradio;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class UntouchableHorizontalScrollView extends HorizontalScrollView {

    public UntouchableHorizontalScrollView(Context context) {
        super(context);
    }

    public UntouchableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UntouchableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
