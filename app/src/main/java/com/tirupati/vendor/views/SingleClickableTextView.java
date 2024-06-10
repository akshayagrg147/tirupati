package com.tirupati.vendor.views;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SingleClickableTextView extends androidx.appcompat.widget.AppCompatTextView {
    public SingleClickableTextView(Context context) {
        this(context, null);
    }

    public SingleClickableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleClickableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private long lastClickTime = 0;

    @Override
    public void setOnClickListener(@Nullable final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();
                if (l != null)
                    l.onClick(v);
            }
        });
    }
}
