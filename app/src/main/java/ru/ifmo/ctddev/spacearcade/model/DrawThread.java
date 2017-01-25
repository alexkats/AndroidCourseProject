package ru.ifmo.ctddev.spacearcade.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class DrawThread {

    private static final int MIN_FPS = 30;
    private static final long DRAW_PERIOD_IN_MILLIS = 1000 / MIN_FPS;
    private static final long DELAY = 0;

    private final GameController gameController;
    private Timer timer;

    public DrawThread(GameController gameController) {
        this.gameController = gameController;
    }

    public void startGame() {
        stopGame();
        timer = new Timer();
        timer.schedule(new DrawTimerTask(), DELAY, DRAW_PERIOD_IN_MILLIS);
    }

    public void stopGame() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public void resumeGame() {
        startGame();
    }

    public void pauseGame() {
        stopGame();
    }

    private class DrawTimerTask extends TimerTask {

        @Override
        public void run() {
            gameController.onDraw();
        }
    }
}
