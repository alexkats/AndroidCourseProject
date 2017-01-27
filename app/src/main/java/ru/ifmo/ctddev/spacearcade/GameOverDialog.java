package ru.ifmo.ctddev.spacearcade;

import android.view.View;

import ru.ifmo.ctddev.spacearcade.model.BaseDialog;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public class GameOverDialog extends BaseDialog implements View.OnClickListener {

    private GameOverDialogListener listener;
    private int selectedId;

    public GameOverDialog(BaseFragment owner) {
        super(owner.getMainActivity());
        setContentView(R.layout.dialog_game_over);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
    }

    public void setListener(GameOverDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        selectedId = v.getId();
        dismiss();
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    protected void onDismissed() {
        if (selectedId == R.id.btn_exit) {
            listener.exitGame();
        } else if (selectedId == R.id.btn_resume) {
            listener.startNewGame();
        }
    }

    public interface GameOverDialogListener {
        void exitGame();

        void startNewGame();
    }
}
