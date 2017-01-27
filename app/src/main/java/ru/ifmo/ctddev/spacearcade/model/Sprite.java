package ru.ifmo.ctddev.spacearcade.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public abstract class Sprite extends ScreenGameObject {

    protected final double pixelFactor;
    private final Bitmap bitmap;
    private final Matrix matrix = new Matrix();
    private final Paint paint = new Paint();
    public double rotation;
    public int alpha = 255;
    public double scale = 1;

    protected Sprite(GameEngine gameEngine, int drawableRes, BodyType bodyType) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        pixelFactor = gameEngine.pixelFactor;

        height = (int) (spriteDrawable.getIntrinsicHeight() * pixelFactor);
        width = (int) (spriteDrawable.getIntrinsicWidth() * pixelFactor);

        bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        radius = Math.max(height, width) / 2.0d;

        bodyType = bodyType;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (x > canvas.getWidth()
                || y > canvas.getHeight()
                || x < -width
                || y < -height) {
            return;
        }

        float scaleFactor = (float) (pixelFactor * scale);
        matrix.reset();
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate((float) x, (float) y);
        matrix.postRotate((float) rotation, (float) (x + width * scale / 2), (float) (y + height * scale / 2));
        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
