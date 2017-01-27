package ru.ifmo.ctddev.spacearcade.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;
import ru.ifmo.ctddev.spacearcade.sound.GameEvent;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class ScoreGameObject extends GameObject {

    private static final int POINT_LOSS_PER_ASTEROID_MISSED = 1;
    private static final int POINTS_GAINED_PER_ASTEROID_HIT = 50;

    private final TextView textView;
    private int points;
    private final Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            CharSequence text = String.format(Locale.US, "%06d", points);
            textView.setText(text);
        }
    };
    private boolean pointsHaveChanged;

    public ScoreGameObject(View view, int viewResId) {
        textView = (TextView) view.findViewById(viewResId);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        points = 0;
        textView.post(updateTextRunnable);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (pointsHaveChanged) {
            textView.post(updateTextRunnable);
            pointsHaveChanged = false;
        }
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.ASTEROID_HIT) {
            points += POINTS_GAINED_PER_ASTEROID_HIT;
            pointsHaveChanged = true;
        } else if (gameEvent == GameEvent.ASTEROID_MISSED) {
            if (points > 0) {
                points -= POINT_LOSS_PER_ASTEROID_MISSED;
            }

            pointsHaveChanged = true;
        }
    }
}
