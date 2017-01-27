package ru.ifmo.ctddev.spacearcade.model;

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

    public abstract void startGame();

    public abstract void onDraw();

    public abstract void onUpdate(long elapsedTimeInMillis, GameController gameController);

    public void onGameObjectAddedToGameUIThread() {

    }

    public void onGameObjectRemovedFromGameUIThread() {

    }
}
