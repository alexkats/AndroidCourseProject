package ru.ifmo.ctddev.spacearcade.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ru.ifmo.ctddev.spacearcade.input.InputController;
import ru.ifmo.ctddev.spacearcade.sound.GameEvent;
import ru.ifmo.ctddev.spacearcade.sound.SoundManager;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class GameEngine {

    public final Random random = new Random();
    private final List<List<GameObject>> layers = new ArrayList<>();
    private final Collection<GameObject> gameObjects = new ArrayList<>();
    private final List<Collision> detectedCollisions = new ArrayList<>();
    private final QuadTree quadTreeRoot = new QuadTree();
    private final Queue<GameObject> gameObjectsAddQueue = new LinkedList<>();
    private final Queue<GameObject> gameObjectsRemoveQueue = new LinkedList<>();
    private final GameView gameView;
    private final Activity activity;
    public InputController inputController;
    public int width;
    public int height;
    public double pixelFactor;
    private UpdateThread updateThread;
    private DrawThread drawThread;
    private SoundManager soundManager;

    public GameEngine(Activity activity, GameView gameView, int layersNum) {
        this.activity = activity;
        this.gameView = gameView;
        this.gameView.setGameObjects(layers);
        QuadTree.init();

        width = gameView.getWidth() - gameView.getPaddingRight() - gameView.getPaddingLeft();
        height = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();
        pixelFactor = height / 400.0d;

        quadTreeRoot.setArea(new Rect(0, 0, width, height));

        for (int i = 0; i < layersNum; i++) {
            layers.add(new ArrayList<GameObject>());
        }
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public void onGameEvent(GameEvent gameEvent) {
        for (GameObject gameObject : gameObjects) {
            gameObject.onGameEvent(gameEvent);
        }

        soundManager.playSoundForGameEvent(gameEvent);
    }

    public void startGame() {
        stopGame();

        for (GameObject gameObject : gameObjects) {
            gameObject.startGame(this);
        }

        if (inputController != null) {
            inputController.onStart();
        }

        updateThread = new UpdateThread(this);
        updateThread.start();

        drawThread = new DrawThread(this);
        drawThread.start();
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

    public void addGameObject(GameObject gameObject, int layer) {
        gameObject.layer = layer;

        if (isGameRunning()) {
            gameObjectsAddQueue.add(gameObject);
        } else {
            addToLayerNow(gameObject);
        }

        activity.runOnUiThread(gameObject.onGameObjectAddedRunnable);
    }

    public boolean isGameRunning() {
        return updateThread != null && updateThread.isGameRunning();
    }

    private void addToLayerNow(GameObject gameObject) {
        int layer = gameObject.layer;

        while (layers.size() <= layer) {
            layers.add(new ArrayList<GameObject>());
        }

        layers.get(layer).add(gameObject);
        gameObjects.add(gameObject);

        if (gameObject instanceof ScreenGameObject) {
            ScreenGameObject screenGameObject = (ScreenGameObject) gameObject;

            if (screenGameObject.bodyType != BodyType.NONE) {
                quadTreeRoot.addGameObject(screenGameObject);
            }
        }

        gameObject.onAddedToGameEngine();
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
            gameObject.onPostUpdate(this);
        }

        checkCollisions();

        synchronized (layers) {
            while (!gameObjectsRemoveQueue.isEmpty()) {
                GameObject gameObject = gameObjectsRemoveQueue.poll();
                gameObjects.remove(gameObject);
                layers.get(gameObject.layer).remove(gameObject);

                if (gameObject instanceof ScreenGameObject) {
                    quadTreeRoot.removeGameObject((ScreenGameObject) gameObject);
                }

                gameObject.onRemovedFromGameEngine();
            }

            while (!gameObjectsAddQueue.isEmpty()) {
                GameObject gameObject = gameObjectsAddQueue.poll();
                addToLayerNow(gameObject);
            }
        }
    }

    private void checkCollisions() {
        while (!detectedCollisions.isEmpty()) {
            Collision.release(detectedCollisions.remove(0));
        }

        quadTreeRoot.checkCollisions(this, detectedCollisions);
    }

    public void onDraw() {
        gameView.draw();
    }

    public boolean isGamePaused() {
        return updateThread != null && updateThread.isPaused();
    }

    public Context getContext() {
        return gameView.getContext();
    }
}
