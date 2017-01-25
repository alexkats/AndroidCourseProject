package ru.ifmo.ctddev.spacearcade.model;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class UpdateThread extends Thread {

    private final GameController gameController;
    private boolean gameRunning = true;
    private boolean paused;

    private final Object lock = new Object();

    public UpdateThread(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public synchronized void start() {
        gameRunning = true;
        super.start();
    }

    public void stopGame() {
        gameRunning = false;
        resumeGame();
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

            gameController.onUpdate(elapsedTimeInMillis);
            previousTimeInMillis = currentTimeInMillis;
        }
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

    public boolean isGamePaused() {
        return paused;
    }
}
