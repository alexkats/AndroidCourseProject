package ru.ifmo.ctddev.spacearcade.counter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.QuitDialog;
import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.sound.SoundManager;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

@SuppressWarnings("RefusedBequest")
public class MainMenuFragment extends BaseFragment implements QuitDialog.QuitDialogListener {

    private static final String SHOULD_SHOW_GAMEPAD_HELP = "ru.ifmo.ctddev.spacearcade.should.show.gamepad.help";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).startGame();
            }
        });

        view.findViewById(R.id.btn_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_sound) {
                    SoundManager soundManager = getMainActivity().getSoundManager();
                    soundManager.toggleSoundStatus();
                    updateSoundAndMusicButtons();
                }
            }
        });

        view.findViewById(R.id.btn_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_music) {
                    SoundManager soundManager = getMainActivity().getSoundManager();
                    soundManager.toggleMusicStatus();
                    updateSoundAndMusicButtons();
                }
            }
        });

        Animation pulseAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_pulse);
        view.findViewById(R.id.btn_start).startAnimation(pulseAnimation);

        updateSoundAndMusicButtons();
    }

    @Override
    protected void onLayoutCompleted() {
        animateTitles();
        Animator animation = ValueAnimator.ofFloat(0f, 42f);
        animation.setDuration(1000);
        animation.start();
    }

    private void animateTitles() {
        if (getView() == null) {
            return;
        }

        View title = getView().findViewById(R.id.main_title);
        Animation titleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.title_enter);
        title.startAnimation(titleAnimation);

        View subtitle = getView().findViewById(R.id.main_subtitle);
        Animation subtitleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.subtitle_enter);
        subtitle.startAnimation(subtitleAnimation);
    }

    @Override
    public boolean onBackPressed() {
        if (!super.onBackPressed()) {
            QuitDialog quitDialog = new QuitDialog(getMainActivity());
            quitDialog.setListener(this);
            showDialog(quitDialog);
        }

        return true;
    }

    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = getMainActivity().getSoundManager();
        boolean music = soundManager.getMusicStatus();

        if (getView() == null) {
            return;
        }

        ImageView btnMusic = (ImageView) getView().findViewById(R.id.btn_music);

        if (music) {
            btnMusic.setImageResource(R.drawable.music_on_no_bg);
        } else {
            btnMusic.setImageResource(R.drawable.music_off_no_bg);
        }

        boolean sound = soundManager.getSoundStatus();
        ImageView btnSound = (ImageView) getView().findViewById(R.id.btn_sound);

        if (sound) {
            btnSound.setImageResource(R.drawable.sounds_on_no_bg);
        } else {
            btnSound.setImageResource(R.drawable.sounds_off_no_bg);
        }
    }

    @Override
    public void exit() {
        getMainActivity().finish();
    }
}
