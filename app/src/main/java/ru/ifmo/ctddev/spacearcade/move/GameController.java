package ru.ifmo.ctddev.spacearcade.move;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES_IN_MILLIS = 500;
    private final List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private long currentMillis;
    private int enemiesSpawned;

    public GameController(GameEngine gameEngine) {
        double pixelFactor = gameEngine.pixelFactor;

        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Do nothing
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;
        long waveTimestamp = enemiesSpawned * TIME_BETWEEN_ENEMIES_IN_MILLIS;

        if (currentMillis > waveTimestamp) {
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);
            a.addToGameEngine(gameEngine, layer);
            enemiesSpawned++;
        }
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }
}
