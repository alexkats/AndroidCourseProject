package ru.ifmo.ctddev.spacearcade.input;

import android.view.MotionEvent;
import android.view.View;

import ru.ifmo.ctddev.spacearcade.R;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class BasicInputController extends InputController implements View.OnTouchListener {

    public BasicInputController(View view) {
        view.findViewById(R.id.keypad_up).setOnTouchListener(this);
        view.findViewById(R.id.keypad_down).setOnTouchListener(this);
        view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        view.findViewById(R.id.keypad_fire).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int actionMask = event.getActionMasked();
        int eventId = v.getId();

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN:
                if (eventId == R.id.keypad_up) {
                    verticalFactor -= 1;
                } else if (eventId == R.id.keypad_down) {
                    verticalFactor += 1;
                } else if (eventId == R.id.keypad_left) {
                    horizontalFactor -= 1;
                } else if (eventId == R.id.keypad_right) {
                    horizontalFactor += 1;
                } else if (eventId == R.id.keypad_fire) {
                    isFiringNow = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (eventId == R.id.keypad_up) {
                    verticalFactor += 1;
                } else if (eventId == R.id.keypad_down) {
                    verticalFactor -= 1;
                } else if (eventId == R.id.keypad_left) {
                    horizontalFactor += 1;
                } else if (eventId == R.id.keypad_right) {
                    horizontalFactor -= 1;
                } else if (eventId == R.id.keypad_fire) {
                    isFiringNow = false;
                }

                break;
        }

        return false;
    }
}
