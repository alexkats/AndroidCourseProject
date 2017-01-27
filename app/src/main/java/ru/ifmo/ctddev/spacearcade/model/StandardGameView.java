package ru.ifmo.ctddev.spacearcade.model;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class StandardGameView extends View implements GameView {

    private List<List<GameObject>> layers;

    public StandardGameView(Context context) {
        super(context);
    }

    public StandardGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw() {
        postInvalidate();
    }

    @Override
    public void setGameObjects(List<List<GameObject>> gameObjects) {
        layers = gameObjects;
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (layers) {
            for (List<GameObject> layer : layers) {
                for (GameObject gameObject : layer) {
                    gameObject.onDraw(canvas);
                }
            }
        }
    }
}
