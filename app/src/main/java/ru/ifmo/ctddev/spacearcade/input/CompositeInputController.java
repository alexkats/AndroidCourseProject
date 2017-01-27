package ru.ifmo.ctddev.spacearcade.input;

import android.view.View;

import ru.ifmo.ctddev.spacearcade.MainActivity;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

@SuppressWarnings("RefusedBequest")
public class CompositeInputController extends InputController {

    private final InputController gamepadInputController;
    private final InputController virtualJoystickInputController;

    public CompositeInputController(View view, MainActivity activity) {
        gamepadInputController = new GamepadInputController(activity);
        virtualJoystickInputController = new VirtualJoystickInputController(view);
    }

    @Override
    public void onStart() {
        gamepadInputController.onStart();
        virtualJoystickInputController.onStart();
    }

    @Override
    public void onResume() {
        gamepadInputController.onResume();
        virtualJoystickInputController.onResume();
    }

    @Override
    public void onStop() {
        gamepadInputController.onStop();
        virtualJoystickInputController.onStop();
    }

    @Override
    public void onPause() {
        gamepadInputController.onPause();
        virtualJoystickInputController.onPause();
    }

    @Override
    public void onUpdate() {
        horizontalFactor = gamepadInputController.horizontalFactor + virtualJoystickInputController.horizontalFactor;
        verticalFactor = gamepadInputController.verticalFactor + virtualJoystickInputController.verticalFactor;
        isFiringNow = gamepadInputController.isFiringNow || virtualJoystickInputController.isFiringNow;
    }
}
