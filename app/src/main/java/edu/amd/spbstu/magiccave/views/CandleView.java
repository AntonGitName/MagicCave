package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import edu.amd.spbstu.magiccave.IO.ResourceLoader;
import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.model.CandleModel;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandleView extends AnimatedView {

    private static final int SOLVE_ANIMATION_ITERATIONS = 256;

    private static final Random RND = new Random();

    private final ResourceLoader mResourceLoader;
    private final CandleImage mCandleImage;
    private final CandleModel mModel;
    private final OnCandleViewClickListener listener;

    private int viewW;
    private int viewH;
    private boolean isSizeSet = false;
    private int solveAnimationTimer;
    private boolean isSolveAnimationShowing;

    private OnAnimationFinishedListener animationFinishedListener;

    public CandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mResourceLoader = MainApplication.RESOURCE_LOADER;
        this.mModel = new CandleModel(1, 1);
        this.mCandleImage = new CandleImage(mModel.getState());
        this.listener = null;

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mModel.inverseWithNeighbours();
            }
        });
    }

    public CandleView(Context ctx, CandleModel model, final OnCandleViewClickListener listener) {
        super(ctx);

        this.mModel = model;
        this.mResourceLoader = MainApplication.RESOURCE_LOADER;
        this.mCandleImage = new CandleImage(model.getState());
        this.listener = listener;

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mModel.inverseWithNeighbours();
                listener.onCandleViewClick(true);
            }
        });
    }

    public CandleModel getModel() {
        return mModel;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewH = h;
        viewW = w;
        isSizeSet = true;
        mCandleImage.loadImages(mResourceLoader, new Point(w, h));
    }

    void drawCandleImage(Canvas canvas) {
        if (isSizeSet) {
            mCandleImage.setState(mModel.getState());

            mCandleImage.incTimer();
            int offsetX = (viewW - mCandleImage.candle.getWidth()) / 2;
            int offsetY = (viewH - mCandleImage.candle.getHeight() - mCandleImage.nextFire.getHeight()) / 2;
            canvas.drawBitmap(mCandleImage.candle, offsetX, offsetY + viewH / 2, mCandleImage.candlePaint);

            offsetX = (viewW - mCandleImage.prevFire.getWidth()) / 2;
            canvas.drawBitmap(mCandleImage.prevFire, offsetX, offsetY, mCandleImage.prevFirePaint);
            canvas.drawBitmap(mCandleImage.nextFire, offsetX, offsetY, mCandleImage.nextFirePaint);

            if (isSolveAnimationShowing) {
                final float t = 6f * (float) Math.PI * (float) solveAnimationTimer / (float) SOLVE_ANIMATION_ITERATIONS;
                final int x = (int) (viewW * (2.25f + Math.cos(t) * 0.25f)) / 4;
                final int y = viewH / 2;
                canvas.drawBitmap(mCandleImage.hand, x, y, mCandleImage.handPaint);
                if (++solveAnimationTimer == SOLVE_ANIMATION_ITERATIONS) {
                    finishSolveAnimation();
                }
            }
        }
    }

    private void finishSolveAnimation() {
        isSolveAnimationShowing = false;
        solveAnimationTimer = 0;
        listener.onCandleViewClick(false);
        animationFinishedListener.onAnimationFinished();
        animationFinishedListener = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCandleImage(canvas);
    }

    private static final class CandleImage {
        private static final int ITERATIONS_PER_FRAME = 16;
        private static final int ITERATIONS_PER_STATE = 64;

        public final Paint candlePaint = new Paint();
        public final Paint handPaint = new Paint();
        public final Paint nextFirePaint = new Paint();
        public final Paint prevFirePaint = new Paint();
        public Bitmap candle;
        public Bitmap hand;
        public Bitmap[] fire;
        public State state;

        private int animationTimer;
        private int stateTimer;
        private int frameNum;

        public Bitmap nextFire;
        public Bitmap prevFire;

        public CandleImage(CandleModel.State modelState) {
            candlePaint.setAntiAlias(true);
            handPaint.setAntiAlias(true);
            nextFirePaint.setAntiAlias(true);
            prevFirePaint.setAntiAlias(true);
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
            prevFirePaint.setAlpha(alpha * stateTimer / (ITERATIONS_PER_STATE - 1));
            nextFirePaint.setAlpha((255 - alpha) * stateTimer / (ITERATIONS_PER_STATE - 1));
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

    public interface OnCandleViewClickListener {
        void onCandleViewClick(boolean needCheck);
    }

    public void playChangeAnimation(OnAnimationFinishedListener listener) {
        this.isSolveAnimationShowing = true;
        this.solveAnimationTimer = 0;
        this.animationFinishedListener = listener;
        this.mModel.inverseWithNeighbours();
    }

    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }
}
