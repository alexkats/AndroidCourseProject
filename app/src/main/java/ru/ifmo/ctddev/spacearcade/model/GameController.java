package ru.ifmo.ctddev.spacearcade.model;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class GameController {

    private final List<GameObject> gameObjects = new ArrayList<>();
    private final Activity activity;

    private UpdateThread updateThread;
    private DrawThread drawThread;

    private final Runnable drawRunnable = new Runnable() {

        @Override
        public void run() {
            for (GameObject gameObject : gameObjects) {
                gameObject.onDraw();
            }
        }
    };

    public GameController(Activity activity) {
        this.activity = activity;
    }

    public void startGame() {
        stopGame();

        for (GameObject gameObject : gameObjects) {
            gameObject.startGame();
        }

        updateThread = new UpdateThread(this);
        updateThread.start();

        drawThread = new DrawThread(this);
        drawThread.startGame();
    }

    public void stopGame() {
        if (updateThread != null) {
            updateThread.stopGame();
        }

        if (drawThread != null) {
            drawThread.stopGame();
        }
    }

    public void resumeGame() {
        if (updateThread != null) {
            updateThread.resumeGame();
        }

        if (drawThread != null) {
            drawThread.resumeGame();
        }
    }

    public void pauseGame() {
        if (updateThread != null) {
            updateThread.pauseGame();
        }

        if (drawThread != null) {
            drawThread.pauseGame();
        }
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void onUpdate(long elapsedTimeInMillis) {
        for (GameObject gameObject : gameObjects) {
            gameObject.onUpdate(elapsedTimeInMillis, this);
        }
    }

    public void onDraw() {
        activity.runOnUiThread(drawRunnable);
    }

    public boolean isGameRunning() {
        return updateThread != null && updateThread.isGameRunning();
    }

    public boolean isGamePaused() {
        return updateThread != null && updateThread.isGamePaused();
    }
}
