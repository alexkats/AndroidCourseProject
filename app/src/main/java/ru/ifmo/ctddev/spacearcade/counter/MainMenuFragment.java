package ru.ifmo.ctddev.spacearcade.counter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.R;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class MainMenuFragment extends BaseFragment {

    private static final String SHOULD_SHOW_GAMEPAD_HELP = "ru.ifmo.ctddev.spacearcade.should.show.gamepad.help";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).startGame();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isGameControllerConnected() && shouldShowGamepadHelp()) {
            showGamepadHelp();

            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putBoolean(SHOULD_SHOW_GAMEPAD_HELP, false)
                    .apply();
        }
    }

    private void showGamepadHelp() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.gamepad_help_title)
                .setMessage(R.string.gamepad_help_message)
                .create()
                .show();
    }

    private boolean shouldShowGamepadHelp() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(SHOULD_SHOW_GAMEPAD_HELP, true);
    }

    public static boolean isGameControllerConnected() {
        for (int deviceId : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(deviceId);
            int sources = device.getSources();
            boolean result = (sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD;
            result |= (sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK;

            if (result) {
                return true;
            }
        }

        return false;
    }
}
