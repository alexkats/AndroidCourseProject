package ru.ifmo.ctddev.spacearcade.move;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctddev.spacearcade.GameOverDialog;
import ru.ifmo.ctddev.spacearcade.counter.GameFragment;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;
import ru.ifmo.ctddev.spacearcade.sound.GameEvent;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 500;
    private static final int ASTEROID_POOL_SIZE = 10;
    private static final int INITIAL_LIFES = 3;
    private static final int STOPPING_WAVE_WAITING_TIME = 2000;
    private static final int NEXT_WAVE_WAITING_TIME = 3000;
    private final GameFragment owner;
    private final List<Asteroid> asteroidPool = new ArrayList<>();
    private long currentMillis;
    private int enemiesSpawned;
    private Object state;
    private int waitingTime;
    private int numLives;

    public GameController(GameEngine gameEngine, GameFragment owner) {
        this.owner = owner;

        for (int i = 0; i < ASTEROID_POOL_SIZE; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        currentMillis = 0;
        enemiesSpawned = 0;
        waitingTime = 0;

        for (int i = 0; i < INITIAL_LIFES; i++) {
            gameEngine.onGameEvent(GameEvent.LIFE_ADDED);
        }

        state = GameControllerState.PLACING_SPACESHIP;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Do nothing
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (state == GameControllerState.SPAWNING_ENEMIES) {
            currentMillis += elapsedMillis;
            long waveTimestamp = enemiesSpawned * TIME_BETWEEN_ENEMIES;

            if (currentMillis > waveTimestamp) {
                Asteroid a = asteroidPool.remove(0);
                a.init(gameEngine);
                a.addToGameEngine(gameEngine, layer);
                enemiesSpawned++;
            }
        } else if (state == GameControllerState.STOPPING_WAVE) {
            waitingTime += elapsedMillis;

            if (waitingTime > STOPPING_WAVE_WAITING_TIME) {
                state = GameControllerState.PLACING_SPACESHIP;
            }
        } else if (state == GameControllerState.PLACING_SPACESHIP) {
            if (numLives == 0) {
                gameEngine.onGameEvent(GameEvent.GAME_OVER);
            } else {
                numLives--;
                gameEngine.onGameEvent(GameEvent.LIFE_LOST);
                GameObject newLife = new Player(gameEngine);
                newLife.addToGameEngine(gameEngine, 3);
                newLife.startGame(gameEngine);
                state = GameControllerState.WAITING;
                waitingTime = 0;
            }
        } else if (state == GameControllerState.WAITING) {
            waitingTime += elapsedMillis;

            if (waitingTime > NEXT_WAVE_WAITING_TIME) {
                state = GameControllerState.SPAWNING_ENEMIES;
            }
        }
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.SPACESHIP_HIT) {
            state = GameControllerState.STOPPING_WAVE;
            waitingTime = 0;
        } else if (gameEvent == GameEvent.GAME_OVER) {
            state = GameControllerState.GAME_OVER;
            showGameOverDialog();
        } else if (gameEvent == GameEvent.LIFE_ADDED) {
            numLives++;
        }
    }

    private void showGameOverDialog() {
        owner.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                GameOverDialog quitDialog = new GameOverDialog(owner);
                quitDialog.setListener(owner);
                owner.showDialog(quitDialog);
            }
        });
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }
}
