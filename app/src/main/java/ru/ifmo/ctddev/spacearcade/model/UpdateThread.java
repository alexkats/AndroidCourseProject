package ru.ifmo.ctddev.spacearcade.model;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class UpdateThread extends Thread {

    private final GameEngine gameEngine;
    private final Object lock = new Object();
    private boolean gameRunning = true;
    private boolean paused;

    public UpdateThread(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void start() {
        gameRunning = true;
        paused = false;
        super.start();
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public void run() {
        long currentTimeInMillis;
        long elapsedTimeInMillis;
        long previousTimeInMillis = System.currentTimeMillis();

        while (gameRunning) {
            currentTimeInMillis = System.currentTimeMillis();
            elapsedTimeInMillis = currentTimeInMillis - previousTimeInMillis;

            if (paused) {
                while (paused) {
                    try {
                        synchronized (lock) {
                            lock.wait();
                        }
                    } catch (InterruptedException ignored) {

                    }
                }

                currentTimeInMillis = System.currentTimeMillis();
            }

            gameEngine.onUpdate(elapsedTimeInMillis);
            previousTimeInMillis = currentTimeInMillis;
        }
    }

    public void stopGame() {
        gameRunning = false;
        resumeGame();
    }

    public void resumeGame() {
        if (paused) {
            paused = false;

            synchronized (lock) {
                lock.notify();
            }
        }
    }

    public void pauseGame() {
        paused = true;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isPaused() {
        return paused;
    }
}
