package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import edu.amd.spbstu.magiccave.IO.ResourceLoader;
import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.interfaces.OnAnimationFinishedListener;
import edu.amd.spbstu.magiccave.interfaces.OnCandleViewClickListener;
import edu.amd.spbstu.magiccave.model.CandleModel;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandleView extends AnimatedView {

    private static final int SOLVE_ANIMATION_ITERATIONS = 128;

    private static final Random RND = new Random();

    private final ResourceLoader resourceLoader;
    private final CandleImage candleImage;
    private final CandleModel candleModel;
    private final OnCandleViewClickListener listener;
    private final Paint selectionCirclePaint;
    private int viewW;
    private int viewH;
    private boolean isSizeSet = false;
    private int solveAnimationTimer;
    private boolean isSolveAnimationShowing;
    private OnAnimationFinishedListener animationFinishedListener;
    private boolean isSelected;

    public CandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.resourceLoader = MainApplication.RESOURCE_LOADER;
        this.candleModel = new CandleModel(1, 1);
        this.candleImage = new CandleImage(candleModel.getState());
        this.listener = null;
        this.selectionCirclePaint = null;

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                candleModel.inverseWithNeighbours();
            }
        });
    }

    public CandleView(Context ctx, CandleModel model, final OnCandleViewClickListener listener) {
        super(ctx);

        this.candleModel = model;
        this.resourceLoader = MainApplication.RESOURCE_LOADER;
        this.candleImage = new CandleImage(model.getState());
        this.listener = listener;
        this.selectionCirclePaint = new Paint();

        selectionCirclePaint.setAntiAlias(true);
        selectionCirclePaint.setColor(Color.BLUE);
        selectionCirclePaint.setStyle(Paint.Style.STROKE);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!listener.isConnecting()) {
                    candleModel.inverseWithNeighbours();
                } else {
                    isSelected = true;
                }
                listener.onCandleViewClick(getModel().getId(), true);
            }
        });
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public CandleModel getModel() {
        return candleModel;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        viewH = h;
        viewW = w;
        isSizeSet = true;
        if (isInEditMode()) {
            return;
        }
        candleImage.loadImages(resourceLoader, new Point(w, h));
    }

    private void drawCandleImage(Canvas canvas) {
        if (isSizeSet) {
            candleImage.setState(candleModel.getState());

            drawCandle(canvas);
            if (isSolveAnimationShowing) {
                drawSolveAnimation(canvas);
            }
        }
    }

    private void drawCandle(Canvas canvas) {
        candleImage.incTimer();
        int offsetX = (viewW - candleImage.candle.getWidth()) / 2;
        int offsetY = (viewH - candleImage.candle.getHeight() - candleImage.nextFire.getHeight()) / 2;
        canvas.drawBitmap(candleImage.candle, offsetX, offsetY + viewH / 2, candleImage.candlePaint);
        offsetX = (viewW - candleImage.prevFire.getWidth()) / 2;
        canvas.drawBitmap(candleImage.prevFire, offsetX, offsetY, candleImage.prevFirePaint);
        canvas.drawBitmap(candleImage.nextFire, offsetX, offsetY, candleImage.nextFirePaint);

        if (isSelected) {
            final float cx = viewW / 2;
            final float cy = viewH / 2;
            canvas.drawCircle(cx, cy, (float) Math.min(viewH, viewW) / 2, selectionCirclePaint);
        }
    }

    private void drawSolveAnimation(Canvas canvas) {
        final float t = 6f * (float) Math.PI * (float) solveAnimationTimer / (float) SOLVE_ANIMATION_ITERATIONS;
        final int x = (int) (viewW * (2.25f + Math.cos(t) * 0.25f)) / 4;
        final int y = viewH / 2;
        canvas.drawBitmap(candleImage.hand, x, y, candleImage.handPaint);
        ++solveAnimationTimer;
        if (solveAnimationTimer == SOLVE_ANIMATION_ITERATIONS / 2) {
            this.candleModel.inverseWithNeighbours();
        }
        if (solveAnimationTimer == SOLVE_ANIMATION_ITERATIONS) {
            finishSolveAnimation();
        }
    }

    private void finishSolveAnimation() {
        isSolveAnimationShowing = false;
        solveAnimationTimer = 0;
        listener.onCandleViewClick(getModel().getId(), false);
        animationFinishedListener.onAnimationFinished();
        animationFinishedListener = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode()) {
            return;
        }

        drawCandleImage(canvas);
    }

    public void playChangeAnimation(OnAnimationFinishedListener listener) {
        this.isSolveAnimationShowing = true;
        this.solveAnimationTimer = 0;
        this.animationFinishedListener = listener;
    }

    private static final class CandleImage {
        private static final int ITERATIONS_PER_FRAME = 6;
        private static final int ITERATIONS_PER_STATE = 64;

        public final Paint candlePaint = new Paint();
        public final Paint handPaint = new Paint();
        public final Paint nextFirePaint = new Paint();
        public final Paint prevFirePaint = new Paint();
        public Bitmap candle;
        public Bitmap hand;
        public Bitmap[] fire;
        public State state;
        public Bitmap nextFire;
        public Bitmap prevFire;
        private int animationTimer;
        private int stateTimer;
        private int frameNum;

        public CandleImage(CandleModel.State modelState) {
            candlePaint.setAntiAlias(true);
            handPaint.setAntiAlias(true);
            nextFirePaint.setAntiAlias(true);
            prevFirePaint.setAntiAlias(true);
            animationTimer = RND.nextInt(ITERATIONS_PER_FRAME);
            switch (modelState) {
                case ON:
                    state = State.LIGHT;
                    stateTimer = ITERATIONS_PER_STATE - 1;
                    break;
                case OFF:
                    state = State.DARK;
                    stateTimer = 0;
                    break;
            }
        }

        public void setState(CandleModel.State modelState) {
            if (state == null) {
                switch (modelState) {
                    case ON:
                        state = State.LIGHT;
                        break;
                    case OFF:
                        state = State.LIGHT;
                        break;
                }
            } else {
                switch (modelState) {
                    case ON:
                        if (state == State.DARK || state == State.GOING_DOWN) {
                            state = State.GOING_UP;
                        }
                        break;
                    case OFF:
                        if (state == State.LIGHT || state == State.GOING_UP) {
                            state = State.GOING_DOWN;
                        }
                        break;
                }
            }
        }

        public void incTimer() {
            ++animationTimer;
            if (animationTimer == ITERATIONS_PER_FRAME) {
                animationTimer = 0;
                frameNum = (frameNum + 1) % fire.length;
                prevFire = fire[frameNum];
                nextFire = fire[(frameNum + 1) % fire.length];
            }

            switch (state) {
                case LIGHT:
                    stateTimer = ITERATIONS_PER_STATE - 1;
                    break;
                case GOING_DOWN:
                    --stateTimer;
                    if (stateTimer == 0) {
                        state = State.DARK;
                    }
                    break;
                case GOING_UP:
                    ++stateTimer;
                    if (stateTimer == ITERATIONS_PER_STATE - 1) {
                        state = State.LIGHT;
                    }
                    break;
                case DARK:
                    stateTimer = 0;
                    break;
            }

            int alpha = animationTimer * 255 / ITERATIONS_PER_FRAME;
            prevFirePaint.setAlpha(255 * stateTimer / (ITERATIONS_PER_STATE - 1));
            nextFirePaint.setAlpha(0);
//            nextFirePaint.setAlpha(alpha * stateTimer / (ITERATIONS_PER_STATE - 1));
//            prevFirePaint.setAlpha((255 - alpha) * stateTimer / (ITERATIONS_PER_STATE - 1));
        }

        public void loadImages(ResourceLoader rl, Point size) {
            size.y /= 2;
            candle = rl.getCandleBitmap(size, true);
            fire = rl.getFireBitmaps(size, true);
            size.x /= 2;
            size.y /= 2;
            hand = rl.getHandBitmap(size, true);
            frameNum = RND.nextInt(fire.length);
            animationTimer = RND.nextInt(ITERATIONS_PER_FRAME);
            prevFire = fire[frameNum];
            nextFire = fire[(frameNum + 1) % fire.length];
        }

        private enum State {
            LIGHT, GOING_DOWN, GOING_UP, DARK
        }
    }
}
