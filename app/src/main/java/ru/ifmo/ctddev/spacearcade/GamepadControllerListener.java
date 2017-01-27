package ru.ifmo.ctddev.spacearcade;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * @author Alexey Katsman
 * @since 26.01.17
 */

public interface GamepadControllerListener {

    boolean handleGenericMotionEvent(MotionEvent motionEvent);

    boolean handleKeyEvent(KeyEvent keyEvent);
}
