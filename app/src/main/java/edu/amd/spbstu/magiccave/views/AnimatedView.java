package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author iAnton
 * @since 09/03/15
 */
public abstract class AnimatedView extends View implements Runnable {

    private static final String TAG = "AnimatedView";
    private static final int FRAME_UPDATE_RATE = 8;

    private final Timer timer = new Timer();

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setupTimer() {
        final Handler handler = new Handler(Looper.getMainLooper());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(AnimatedView.this);
            }
        }, FRAME_UPDATE_RATE, FRAME_UPDATE_RATE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        timer.cancel();
        timer.purge();
    }

    @Override
    public void run() {
        invalidate();
    }
}
