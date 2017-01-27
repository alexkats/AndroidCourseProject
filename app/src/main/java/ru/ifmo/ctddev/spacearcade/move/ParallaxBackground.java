package ru.ifmo.ctddev.spacearcade.move;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class ParallaxBackground extends GameObject {

    private final Rect srcRect = new Rect();
    private final Rect dstRect = new Rect();

    private final double imageHeight;
    private final Bitmap bitmap;
    private final double speedY;

    private final double screenHeight;

    private final Matrix matrix = new Matrix();

    private final double pixelFactor;

    protected double positionY;

    public ParallaxBackground(GameEngine gameEngine, int speed, int drawableResId) {
        Drawable spriteDrawable = gameEngine.getContext().getResources().getDrawable(drawableResId);
        bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        pixelFactor = gameEngine.pixelFactor;
        speedY = speed * pixelFactor / 1000d;

        imageHeight = spriteDrawable.getIntrinsicHeight() * pixelFactor;
        screenHeight = gameEngine.height;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        doDraw(canvas);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedY * elapsedMillis;
    }

    public void doDraw(Canvas canvas) {
        if (positionY > 0) {
            matrix.reset();
            matrix.postScale((float) pixelFactor, (float) pixelFactor);
            matrix.postTranslate(0, (float) (positionY - imageHeight));
            canvas.drawBitmap(bitmap, matrix, null);
        }

        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate(0, (float) positionY);
        canvas.drawBitmap(bitmap, matrix, null);

        if (positionY > screenHeight) {
            positionY -= imageHeight;
        }
    }
}
