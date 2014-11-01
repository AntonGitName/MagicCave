package ru.dinter.magiccave.IO;

import java.util.HashMap;
import java.util.Map;

import ru.dinter.magiccave.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

public class ResourceLoader {

    private static final String TAG = "ResourceLoader";

    private final Bitmap candleBitmap;
    private final float candleSizeRate;
    private final Map<Point, Bitmap> candleBitmapScaled = new HashMap<>();

    private static final int[] fireBitmapsIDs = { R.drawable.fire1, R.drawable.fire2, R.drawable.fire3,
            R.drawable.fire4, R.drawable.fire5, R.drawable.fire6, R.drawable.fire7, R.drawable.fire8, R.drawable.fire9,
            R.drawable.fire10, R.drawable.fire11, R.drawable.fire12, R.drawable.fire13, R.drawable.fire14,
            R.drawable.fire15 };

    private final Bitmap[] fireBitmaps;
    private final float fireSizeRate;
    private final Map<Point, Bitmap[]> fireBitmapsScaled = new HashMap<>();

    private static ResourceLoader instance = null;
    
    /*
     * Works fine only if used in exactly one activity!
     */
    public static ResourceLoader newInstance(Resources resources) {
        if (instance == null) {
            instance = new ResourceLoader(resources);
        }
        return instance;
    }
    
    private ResourceLoader(Resources resources) {
        
        Log.d(TAG, "Resource loader created");
        
        candleBitmap = BitmapFactory.decodeResource(resources, R.drawable.candle);
        candleSizeRate = (float) candleBitmap.getWidth() / (float) candleBitmap.getHeight();
        fireBitmaps = new Bitmap[fireBitmapsIDs.length];
        for (int i = 0; i < fireBitmapsIDs.length; ++i) {
            fireBitmaps[i] = BitmapFactory.decodeResource(resources, fireBitmapsIDs[i]);
        }
        fireSizeRate = (float) fireBitmaps[0].getWidth() / (float) fireBitmaps[0].getHeight();
    }

    public Bitmap getCandleBitmap() {
        return candleBitmap;
    }

    public Bitmap[] getFireBitmaps() {
        return fireBitmaps;
    }

    public Bitmap getCandleBitmap(Point size) {
        Point adjustedSize = new Point(Math.min(size.x, (int) (candleSizeRate * size.y)), Math.min(size.y,
                (int) (size.x / candleSizeRate)));
        if (!candleBitmapScaled.containsKey(adjustedSize)) {
            Log.d(TAG, "Loading candle bitmap with size: " + adjustedSize);
            Bitmap bm = Bitmap.createScaledBitmap(candleBitmap, adjustedSize.x, adjustedSize.y, false);
            candleBitmapScaled.put(adjustedSize, bm);
            return bm;
        }
        return candleBitmapScaled.get(adjustedSize);
    }

    public Bitmap[] getFireBitmaps(Point size) {
        Point adjustedSize = new Point(Math.min(size.x, (int) (fireSizeRate * size.y)), Math.min(size.y,
                (int) (size.x / fireSizeRate)));
        if (!fireBitmapsScaled.containsKey(adjustedSize)) {
            Bitmap[] fireBitmapsS = new Bitmap[fireBitmaps.length];
            for (int i = 0; i < fireBitmaps.length; ++i) {
                fireBitmapsS[i] = Bitmap.createScaledBitmap(fireBitmaps[i], adjustedSize.x, adjustedSize.y, false);
            }
            fireBitmapsScaled.put(adjustedSize, fireBitmapsS);
            return fireBitmapsS;
        }
        return fireBitmapsScaled.get(adjustedSize);
    }
}
