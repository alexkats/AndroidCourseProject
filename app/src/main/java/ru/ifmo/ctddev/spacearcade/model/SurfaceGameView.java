package ru.ifmo.ctddev.spacearcade.model;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback, GameView {

    private Iterable<List<GameObject>> layers;

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean ready;

    public SurfaceGameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ready = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ready = false;
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Override
    public void draw() {
        if (!ready) {
            return;
        }

        Canvas canvas = getHolder().lockCanvas();

        if (canvas == null) {
            return;
        }

        canvas.drawRGB(0, 0, 0);

        synchronized (layers) {
            for (Iterable<GameObject> layer : layers) {
                for (GameObject gameObject : layer) {
                    gameObject.onDraw(canvas);
                }
            }
        }

        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void setGameObjects(List<List<GameObject>> gameObjects) {
        layers = gameObjects;
    }
}
