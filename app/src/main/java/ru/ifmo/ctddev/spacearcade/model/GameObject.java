package ru.ifmo.ctddev.spacearcade.model;

import android.graphics.Canvas;

import ru.ifmo.ctddev.spacearcade.sound.GameEvent;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public abstract class GameObject {

    public final Runnable onGameObjectAddedRunnable = new Runnable() {
        @Override
        public void run() {
            onGameObjectAddedToGameUIThread();
        }
    };
    public final Runnable onGameObjectRemovedRunnable = new Runnable() {
        @Override
        public void run() {
            onGameObjectRemovedFromGameUIThread();
        }
    };
    public int layer;

    public abstract void startGame(GameEngine gameEngine);

    public abstract void onDraw(Canvas canvas);

    public abstract void onUpdate(long elapsedTimeInMillis, GameEngine gameEngine);

    public void onGameObjectAddedToGameUIThread() {

    }

    public void onGameObjectRemovedFromGameUIThread() {

    }

    public void onPostUpdate(GameEngine gameEngine) {

    }

    public void addToGameEngine(GameEngine gameEngine, int layer) {
        gameEngine.addGameObject(this, layer);
    }

    public void removeFromGameEngine(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    public void onAddedToGameEngine() {
    }

    public void onRemovedFromGameEngine() {
    }

    public void onGameEvent(GameEvent gameEvent) {

    }
}
