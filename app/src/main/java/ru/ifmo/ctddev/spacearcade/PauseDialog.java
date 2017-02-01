package ru.ifmo.ctddev.spacearcade;

import android.view.View;
import android.widget.ImageView;

import ru.ifmo.ctddev.spacearcade.model.BaseDialog;
import ru.ifmo.ctddev.spacearcade.sound.SoundManager;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public class PauseDialog extends BaseDialog implements View.OnClickListener {

    private PauseDialogListener listener;
    private int selectedId;

    public PauseDialog(MainActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_pause);
        findViewById(R.id.btn_music).setOnClickListener(this);
        findViewById(R.id.btn_sound).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
        updateSoundAndMusicButtons();
    }

    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = owner.getSoundManager();
        boolean music = soundManager.getMusicStatus();
        ImageView btnMusic = (ImageView) findViewById(R.id.btn_music);

        if (music) {
            btnMusic.setImageResource(R.drawable.music_on_no_bg);
        } else {
            btnMusic.setImageResource(R.drawable.music_off_no_bg);
        }

        boolean sound = soundManager.getSoundStatus();
        ImageView btnSounds = (ImageView) findViewById(R.id.btn_sound);

        if (sound) {
            btnSounds.setImageResource(R.drawable.sounds_on_no_bg);
        } else {
            btnSounds.setImageResource(R.drawable.sounds_off_no_bg);
        }
    }

    public void setListener(PauseDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sound || v.getId() == R.id.btn_music) {
            if (v.getId() == R.id.btn_sound) {
                owner.getSoundManager().toggleSoundStatus();
            } else {
                owner.getSoundManager().toggleMusicStatus();
            }

            updateSoundAndMusicButtons();
        } else if (v.getId() == R.id.btn_exit || v.getId() == R.id.btn_resume) {
            selectedId = v.getId();
            super.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        selectedId = R.id.btn_resume;
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    protected void onDismissed() {
        if (selectedId == R.id.btn_exit) {
            listener.exitGame();
        } else if (selectedId == R.id.btn_resume) {
            listener.resumeGame();
        }
    }

    public interface PauseDialogListener {

        void exitGame();

        void resumeGame();
    }
}
