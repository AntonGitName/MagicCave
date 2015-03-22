package edu.amd.spbstu.magiccave.IO;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.amd.spbstu.magiccave.R;

/**
 * @author Anton
 * @since 23.02.2015
 */
public class ResourceLoader {

    private static final String TAG = "ResourceLoader";
    private static final String TYPEFACE_FILENAME = "fonts/AGREVEO.ttf";
    private static final int[] fireBitmapsIDs = {R.drawable.fire1, R.drawable.fire2, R.drawable.fire3,
            R.drawable.fire4, R.drawable.fire5, R.drawable.fire6, R.drawable.fire7, R.drawable.fire8, R.drawable.fire9,
            R.drawable.fire10, R.drawable.fire11, R.drawable.fire12};
//    private static final int[] fireBitmapsIDs = {R.drawable.fire1, R.drawable.fire2, R.drawable.fire3,
//            R.drawable.fire4, R.drawable.fire5, R.drawable.fire6, R.drawable.fire7, R.drawable.fire8, R.drawable.fire9,
//            R.drawable.fire10, R.drawable.fire11, R.drawable.fire12, R.drawable.fire13, R.drawable.fire14,
//            R.drawable.fire15};
    private final Typeface typeface;
    private final Bitmap[] fireBitmaps;
    private final Bitmap candleBitmap;
    private final Bitmap handBitmap;
    private final float fireSizeRate;
    private final float handSizeRate;
    private final float candleSizeRate;
    private final Map<Point, Bitmap[]> fireBitmapsScaled = new HashMap<>();
    private final Map<Point, Bitmap> candleBitmapScaled = new HashMap<>();
    private final Map<Point, Bitmap> handBitmapScaled = new HashMap<>();

    public ResourceLoader(final Resources resources, final AssetManager assetManager) {

        Log.d(TAG, "Resource loader created");

        typeface = Typeface.createFromAsset(assetManager, TYPEFACE_FILENAME);

        handBitmap = BitmapFactory.decodeResource(resources, R.drawable.hand_left);
        handSizeRate = (float) handBitmap.getWidth() / (float) handBitmap.getHeight();
        candleBitmap = BitmapFactory.decodeResource(resources, R.drawable.candle);
        candleSizeRate = (float) candleBitmap.getWidth() / (float) candleBitmap.getHeight();
        fireBitmaps = new Bitmap[fireBitmapsIDs.length];
        for (int i = 0; i < fireBitmapsIDs.length; ++i) {
            fireBitmaps[i] = BitmapFactory.decodeResource(resources, fireBitmapsIDs[i]);
        }
//        fireBitmaps = addMiddleStages(resources, fireBitmapsIDs);

        fireSizeRate = (float) fireBitmaps[0].getWidth() / (float) fireBitmaps[0].getHeight();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public Bitmap getHandBitmap() {
        return handBitmap;
    }

    public Bitmap getCandleBitmap() {
        return candleBitmap;
    }

    public Bitmap[] getFireBitmaps() {
        return fireBitmaps;
    }

    public Bitmap getCandleBitmap(Point size) {
        if (!candleBitmapScaled.containsKey(size)) {
            Log.d(TAG, "Loading candle bitmap with size: " + size);
            Bitmap bm = Bitmap.createScaledBitmap(candleBitmap, size.x, size.y, false);
            candleBitmapScaled.put(size, bm);
            return bm;
        }
        return candleBitmapScaled.get(size);
    }

    public Bitmap getCandleBitmap(Point size, boolean scaledProportional) {
        if (!scaledProportional) {
            return getCandleBitmap(size);
        } else {
            Point adjustedSize = new Point(Math.min(size.x, (int) (candleSizeRate * size.y)), Math.min(size.y,
                    (int) (size.x / candleSizeRate)));
            return getCandleBitmap(adjustedSize);
        }
    }

    public Bitmap[] getFireBitmaps(Point size) {
        if (!fireBitmapsScaled.containsKey(size)) {
            Bitmap[] fireBitmapsS = new Bitmap[fireBitmaps.length];
            for (int i = 0; i < fireBitmaps.length; ++i) {
                fireBitmapsS[i] = Bitmap.createScaledBitmap(fireBitmaps[i], size.x, size.y, false);
            }
            fireBitmapsScaled.put(size, fireBitmapsS);
            return fireBitmapsS;
        }
        return fireBitmapsScaled.get(size);
    }

    public Bitmap[] getFireBitmaps(Point size, boolean scaledProportional) {
        if (!scaledProportional) {
            return getFireBitmaps(size);
        } else {
            Point adjustedSize = new Point(Math.min(size.x, (int) (fireSizeRate * size.y)), Math.min(size.y,
                    (int) (size.x / fireSizeRate)));
            return getFireBitmaps(adjustedSize);
        }
    }

    public Bitmap getHandBitmap(Point size) {
        if (!handBitmapScaled.containsKey(size)) {
            Log.d(TAG, "Loading hand bitmap with size: " + size);
            Bitmap bm = Bitmap.createScaledBitmap(handBitmap, size.x, size.y, false);
            handBitmapScaled.put(size, bm);
            return bm;
        }
        return handBitmapScaled.get(size);
    }

    public Bitmap getHandBitmap(Point size, boolean scaledProportional) {
        if (!scaledProportional) {
            return getHandBitmap(size);
        } else {
            Point adjustedSize = new Point(Math.min(size.x, (int) (handSizeRate * size.y)), Math.min(size.y,
                    (int) (size.x / handSizeRate)));
            return getHandBitmap(adjustedSize);
        }
    }

    private static Bitmap[] addMiddleStages(final Resources res, final int[] bitmapIds) {
        final List<Bitmap> result = new ArrayList<>(bitmapIds.length * 4);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap prev = BitmapFactory.decodeResource(res, bitmapIds[0], options);
        result.add(prev);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        for (int i = 1; i < bitmapIds.length; ++i) {
            final Bitmap next =  BitmapFactory.decodeResource(res, bitmapIds[i], options);
            final Bitmap mid2 = blendBitmaps(res, prev, next);
            final Bitmap mid1 =  blendBitmaps(res, prev, mid2);
            final Bitmap mid3 =  blendBitmaps(res, mid2, next);
            result.add(mid1);
            result.add(mid2);
            result.add(mid3);
            result.add(next);
            prev = next;
        }
        return result.toArray(new Bitmap[result.size()]);
    }

    private static Bitmap blendBitmaps(final Resources res, final Bitmap base, final Bitmap blend) {
        final Bitmap result = base.copy(Bitmap.Config.ARGB_8888, true);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        p.setShader(new BitmapShader(blend, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Canvas c = new Canvas();
        c.setBitmap(result);
        c.drawBitmap(base, 0, 0, null);
        c.drawRect(0, 0, base.getWidth(), base.getHeight(), p);

        return result;
    }
}
