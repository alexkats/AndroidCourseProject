package ru.ifmo.ctddev.spacearcade.model;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public abstract class AnimatedSprite extends Sprite {

    private final AnimationDrawable animationDrawable;
    private int totalTime;
    private long currentTime;

    protected AnimatedSprite(GameEngine gameEngine, int drawableRes, BodyType bodyType) {
        super(gameEngine, drawableRes, bodyType);
        animationDrawable = (AnimationDrawable) spriteDrawable;
        totalTime = 0;

        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            totalTime += animationDrawable.getDuration(i);
        }
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    protected Bitmap obtainDefaultBitmap() {
        AnimationDrawable ad = (AnimationDrawable) spriteDrawable;
        return ((BitmapDrawable) ad.getFrame(0)).getBitmap();
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentTime += elapsedMillis;

        if (currentTime > totalTime) {
            if (animationDrawable.isOneShot()) {
                return;
            } else {
                currentTime %= totalTime;
            }
        }

        long animationElapsedTime = 0;

        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            animationElapsedTime += animationDrawable.getDuration(i);

            if (animationElapsedTime > currentTime) {
                bitmap = ((BitmapDrawable) animationDrawable.getFrame(i)).getBitmap();
                break;
            }
        }
    }
}
