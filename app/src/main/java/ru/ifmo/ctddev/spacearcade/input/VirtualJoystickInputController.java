package ru.ifmo.ctddev.spacearcade.input;

import android.view.MotionEvent;
import android.view.View;

import ru.ifmo.ctddev.spacearcade.R;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class VirtualJoystickInputController extends InputController {

    private final double maxDistance;

    private float initialPositionX;
    private float initialPositionY;

    public VirtualJoystickInputController(View view) {
        view.findViewById(R.id.virtual_joystick_main).setOnTouchListener(new VirtualJoystickOnTouchListener());
        view.findViewById(R.id.virtual_joystick_touch).setOnTouchListener(new VirtualFireButtonOnTouchListener());

        double pixelFactor = view.getHeight() / 400.0d;
        maxDistance = 50 * pixelFactor;
    }

    private class VirtualJoystickOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int actionMask = event.getActionMasked();

            switch (actionMask) {
                case MotionEvent.ACTION_DOWN:
                    initialPositionX = event.getX(0);
                    initialPositionY = event.getY(0);
                    break;
                case MotionEvent.ACTION_UP:
                    horizontalFactor = 0;
                    verticalFactor = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    horizontalFactor = (event.getX(0) - initialPositionX) / maxDistance;
                    verticalFactor = (event.getY(0) - initialPositionY) / maxDistance;

                    if (horizontalFactor > 1) {
                        horizontalFactor = 1;
                    } else if (horizontalFactor < -1) {
                        horizontalFactor = -1;
                    }

                    if (verticalFactor > 1) {
                        verticalFactor = 1;
                    } else if (verticalFactor < -1) {
                        verticalFactor = -1;
                    }

                    break;
            }

            return true;
        }
    }

    private class VirtualFireButtonOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int actionMask = event.getActionMasked();

            if (actionMask == MotionEvent.ACTION_DOWN) {
                isFiringNow = true;
            } else if (actionMask == MotionEvent.ACTION_UP) {
                isFiringNow = false;
            }

            return true;
        }
    }
}
