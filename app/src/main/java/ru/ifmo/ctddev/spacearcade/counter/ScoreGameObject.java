package ru.ifmo.ctddev.spacearcade.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class ScoreGameObject extends GameObject {

    private final TextView textView;
    private long totalTimeInMillis;

    public ScoreGameObject(View view, int id) {
        textView = (TextView) view.findViewById(id);
    }

    @Override
    public void startGame() {
        totalTimeInMillis = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        textView.setText(String.valueOf(totalTimeInMillis));
    }

    @Override
    public void onUpdate(long elapsedTimeInMillis, GameEngine gameEngine) {
        totalTimeInMillis += elapsedTimeInMillis;
    }
}
