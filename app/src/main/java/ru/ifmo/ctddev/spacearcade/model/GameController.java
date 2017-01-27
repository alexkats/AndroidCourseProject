package ru.ifmo.ctddev.spacearcade.model;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import ru.ifmo.ctddev.spacearcade.input.InputController;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class GameController {

    private final Collection<GameObject> gameObjects = new ArrayList<>();

    private final Queue<GameObject> gameObjectsAddQueue = new LinkedList<>();
    private final Queue<GameObject> gameObjectsRemoveQueue = new LinkedList<>();

    private final Activity activity;
    private final Runnable drawRunnable = new Runnable() {

        @Override
        public void run() {
            synchronized (gameObjects) {
                for (GameObject gameObject : gameObjects) {
                    gameObject.onDraw();
                }
            }
        }
    };
    public InputController inputController;
    private UpdateThread updateThread;
    private DrawThread drawThread;

    public GameController(Activity activity) {
        this.activity = activity;
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public void startGame() {
        stopGame();

        for (GameObject gameObject : gameObjects) {
            gameObject.startGame();
        }

        if (inputController != null) {
            inputController.onStart();
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

        if (inputController != null) {
            inputController.onStop();
        }
    }

    public void resumeGame() {
        if (updateThread != null) {
            updateThread.resumeGame();
        }

        if (drawThread != null) {
            drawThread.resumeGame();
        }

        if (inputController != null) {
            inputController.onResume();
        }
    }

    public void pauseGame() {
        if (updateThread != null) {
            updateThread.pauseGame();
        }

        if (drawThread != null) {
            drawThread.pauseGame();
        }

        if (inputController != null) {
            inputController.onPause();
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (isGameRunning()) {
            gameObjectsAddQueue.add(gameObject);
        } else {
            gameObjects.add(gameObject);
        }

        activity.runOnUiThread(gameObject.onGameObjectAddedRunnable);
    }

    public boolean isGameRunning() {
        return updateThread != null && updateThread.isGameRunning();
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjectsRemoveQueue.add(gameObject);
        activity.runOnUiThread(gameObject.onGameObjectRemovedRunnable);
    }

    public void onUpdate(long elapsedTimeInMillis) {
        if (inputController != null) {
            inputController.onUpdate();
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.onUpdate(elapsedTimeInMillis, this);
        }

        synchronized (gameObjects) {
            while (!gameObjectsRemoveQueue.isEmpty()) {
                gameObjects.remove(gameObjectsRemoveQueue.poll());
            }

            while (!gameObjectsAddQueue.isEmpty()) {
                gameObjects.add(gameObjectsAddQueue.poll());
            }
        }
    }

    public void onDraw() {
        activity.runOnUiThread(drawRunnable);
    }

    public boolean isGamePaused() {
        return updateThread != null && updateThread.isGamePaused();
    }
}
