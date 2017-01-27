package ru.ifmo.ctddev.spacearcade.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class FPSCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private final Paint paint;
    private long totalMillis;
    private int draws;

    private String fpsText = "";

    public FPSCounter(GameEngine gameEngine) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        textHeight = (float) (25 * gameEngine.pixelFactor);
        textWidth = (float) (50 * gameEngine.pixelFactor);
        paint.setTextSize(textHeight / 2);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        totalMillis = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, (int) (canvas.getHeight() - textHeight), textWidth, canvas.getHeight(), paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(fpsText, textWidth / 2, (int) (canvas.getHeight() - textHeight / 2), paint);
        draws++;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        totalMillis += elapsedMillis;

        if (totalMillis > 1000) {
            @SuppressWarnings("IntegerDivisionInFloatingPointContext")
            float fps = draws * 1000 / totalMillis;
            fpsText = fps + " fps";
            totalMillis = 0;
            draws = 0;
        }
    }
}
