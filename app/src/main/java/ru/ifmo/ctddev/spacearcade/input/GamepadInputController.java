package ru.ifmo.ctddev.spacearcade.input;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import ru.ifmo.ctddev.spacearcade.GamepadControllerListener;
import ru.ifmo.ctddev.spacearcade.MainActivity;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

@SuppressWarnings("RefusedBequest")
public class GamepadInputController extends InputController implements GamepadControllerListener {

    private final MainActivity activity;

    public GamepadInputController(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onStart() {
        activity.setGamepadControllerListener(this);
    }

    @Override
    public void onResume() {
        horizontalFactor = 0;
        verticalFactor = 0;
    }

    @Override
    public void onStop() {
        activity.setGamepadControllerListener(null);
    }

    @Override
    public boolean handleGenericMotionEvent(MotionEvent motionEvent) {
        int source = motionEvent.getSource();
        boolean result = false;

        if ((source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK) {
            horizontalFactor = motionEvent.getAxisValue(MotionEvent.AXIS_X);
            verticalFactor = motionEvent.getAxisValue(MotionEvent.AXIS_Y);
            InputDevice inputDevice = motionEvent.getDevice();
            InputDevice.MotionRange motionRangeX = inputDevice.getMotionRange(MotionEvent.AXIS_X, source);
            InputDevice.MotionRange motionRangeY = inputDevice.getMotionRange(MotionEvent.AXIS_Y, source);

            if (Math.abs(horizontalFactor) <= motionRangeX.getFlat()) {
                horizontalFactor = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
                InputDevice.MotionRange rangeHatX = inputDevice.getMotionRange(MotionEvent.AXIS_HAT_X, source);

                if (Math.abs(horizontalFactor) <= rangeHatX.getFlat()) {
                    horizontalFactor = 0;
                }
            }

            if (Math.abs(verticalFactor) <= motionRangeY.getFlat()) {
                verticalFactor = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
                InputDevice.MotionRange rangeHatY = inputDevice.getMotionRange(MotionEvent.AXIS_HAT_Y, source);

                if (Math.abs(verticalFactor) <= rangeHatY.getFlat()) {
                    verticalFactor = 0;
                }
            }

            result = true;
        }

        return result;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent) {
        int action = keyEvent.getAction();
        int keyCode = keyEvent.getKeyCode();
        boolean result = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                result = true;

                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    verticalFactor -= 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    verticalFactor += 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    horizontalFactor -= 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    horizontalFactor += 1;
                } else if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                    isFiringNow = true;
                } else {
                    result = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                result = true;

                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    verticalFactor += 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    verticalFactor -= 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    horizontalFactor += 1;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    horizontalFactor -= 1;
                } else if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
                    isFiringNow = false;
                } else if (keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                    activity.onBackPressed();
                } else {
                    result = false;
                }

                break;
        }

        return result;
    }
}
