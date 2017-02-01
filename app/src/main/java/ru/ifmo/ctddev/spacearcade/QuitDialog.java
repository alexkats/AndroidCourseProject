package ru.ifmo.ctddev.spacearcade;

import android.view.View;

import ru.ifmo.ctddev.spacearcade.model.BaseDialog;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public class QuitDialog extends BaseDialog implements View.OnClickListener {

    private QuitDialogListener listener;
    private int selectedId;

    public QuitDialog(MainActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_quit);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
    }

    public void setListener(QuitDialogListener listener) {
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
        if (selectedId == R.id.btn_resume) {
            listener.exit();
        }
    }

    public interface QuitDialogListener {
        void exit();
    }
}
