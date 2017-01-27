package ru.ifmo.ctddev.spacearcade.counter;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;
import ru.ifmo.ctddev.spacearcade.sound.GameEvent;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public class LivesCounter extends GameObject {

    private final ViewGroup layout;
    private final Runnable removeLifeRunnable = new Runnable() {
        @Override
        public void run() {
            if (layout.getChildCount() > 0) {
                layout.removeViewAt(layout.getChildCount() - 1);
            }
        }
    };
    private final Runnable addLifeRunnable = new Runnable() {
        @Override
        public void run() {
            View.inflate(layout.getContext(), R.layout.view_spaceship, layout);
        }
    };

    public LivesCounter(View view, int viewResId) {
        layout = (ViewGroup) view.findViewById(viewResId);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
    }

    @Override
    public void onDraw(Canvas canvas) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.LIFE_LOST) {
            layout.post(removeLifeRunnable);
        } else if (gameEvent == GameEvent.LIFE_ADDED) {
            layout.post(addLifeRunnable);
        }
    }
}
