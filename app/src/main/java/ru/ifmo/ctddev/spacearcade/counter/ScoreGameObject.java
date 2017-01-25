package ru.ifmo.ctddev.spacearcade.counter;

import android.view.View;
import android.widget.TextView;

import ru.ifmo.ctddev.spacearcade.model.GameController;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class ScoreGameObject implements GameObject {

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
    public void onDraw() {
        textView.setText(String.valueOf(totalTimeInMillis));
    }

    @Override
    public void onUpdate(long elapsedTimeInMillis, GameController gameController) {
        totalTimeInMillis += elapsedTimeInMillis;
    }
}
