package ru.ifmo.ctddev.spacearcade.model;

import android.content.Context;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public interface GameView {

    void draw();

    void setGameObjects(List<List<GameObject>> gameObjects);

    int getWidth();

    int getHeight();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getPaddingBottom();

    Context getContext();
}
