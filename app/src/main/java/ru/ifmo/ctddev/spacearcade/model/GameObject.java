package ru.ifmo.ctddev.spacearcade.model;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public interface GameObject {

    void startGame();

    void onDraw();

    void onUpdate(long elapsedTimeInMillis, GameController gameController);
}
