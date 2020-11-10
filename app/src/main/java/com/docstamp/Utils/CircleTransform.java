package com.docstamp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class CircleTransform extends BitmapTransformation {

    private static Paint paintBorder;
    private static BitmapShader shader;

    public CircleTransform(Context context) {
        super(context);
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        if (paintBorder != null)
            paintBorder.setColor(Color.BLUE);

        paintBorder.setAntiAlias(true);
        paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }


    @Override public String getId() {
        return getClass().getName();
    }
}
