package ru.dinter.magiccave.IO;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ru.dinter.magiccave.R;

public class ResourceLoader {

    private static ResourceLoader instance = null;
    private static Resources resources = null;
    
    private static Bitmap candleBitmap;
    
    private static final int FIRE_BITMAPS = 15;
    private static final Bitmap[] fireBitmaps = new Bitmap[FIRE_BITMAPS];
    
    private ResourceLoader() {
        candleBitmap = BitmapFactory.decodeResource(resources, R.drawable.candle);
        fireBitmaps[0] = BitmapFactory.decodeResource(resources, R.drawable.fire1);
        fireBitmaps[1] = BitmapFactory.decodeResource(resources, R.drawable.fire2);
        fireBitmaps[2] = BitmapFactory.decodeResource(resources, R.drawable.fire3);
        fireBitmaps[3] = BitmapFactory.decodeResource(resources, R.drawable.fire4);
        fireBitmaps[4] = BitmapFactory.decodeResource(resources, R.drawable.fire5);
        fireBitmaps[5] = BitmapFactory.decodeResource(resources, R.drawable.fire6);
        fireBitmaps[6] = BitmapFactory.decodeResource(resources, R.drawable.fire7);
        fireBitmaps[7] = BitmapFactory.decodeResource(resources, R.drawable.fire8);
        fireBitmaps[8] = BitmapFactory.decodeResource(resources, R.drawable.fire9);
        fireBitmaps[9] = BitmapFactory.decodeResource(resources, R.drawable.fire10);
        fireBitmaps[10] = BitmapFactory.decodeResource(resources, R.drawable.fire11);
        fireBitmaps[11] = BitmapFactory.decodeResource(resources, R.drawable.fire12);
        fireBitmaps[12] = BitmapFactory.decodeResource(resources, R.drawable.fire13);
        fireBitmaps[13] = BitmapFactory.decodeResource(resources, R.drawable.fire14);
        fireBitmaps[14] = BitmapFactory.decodeResource(resources, R.drawable.fire15);
    }
    
    public static ResourceLoader newInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }
    
    public static void setResources(Resources resources) {
        ResourceLoader.resources = resources;
    }

    public Bitmap getCandleBitmap() {
        return candleBitmap;
    }

    public Bitmap[] getFireBitmaps() {
        return fireBitmaps;
    }
}
