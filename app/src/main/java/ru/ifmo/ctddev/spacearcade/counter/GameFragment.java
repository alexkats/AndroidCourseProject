package ru.ifmo.ctddev.spacearcade.counter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.model.GameController;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class GameFragment extends BaseFragment {

    private GameController gameController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameController = new GameController(getActivity());
        gameController.addGameObject(new ScoreGameObject(view, R.id.text_view_score));

        view.findViewById(R.id.btn_play_pause).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_play_pause) {
                    pauseGameAndShowPauseScreen();
                }
            }
        });

        gameController.startGame();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (gameController.isGameRunning()) {
            pauseGameAndShowPauseScreen();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameController.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        boolean result = false;

        if (gameController.isGameRunning()) {
            pauseGameAndShowPauseScreen();
            result = true;
        }

        return result;
    }

    private void pauseGameAndShowPauseScreen() {
        gameController.pauseGame();

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_screen_title)
                .setMessage(R.string.pause_screen_message)
                .setPositiveButton(R.string.resume_game, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameController.resumeGame();
                    }
                })
                .setNegativeButton(R.string.stop_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameController.stopGame();
                        ((MainActivity) getActivity()).goBack();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        gameController.resumeGame();
                    }
                })
                .create().show();
    }

    private void playOrPause() {
        View view = getView();

        if (view != null) {
            Button button = (Button) view.findViewById(R.id.btn_play_pause);

            if (gameController.isGamePaused()) {
                gameController.resumeGame();
                button.setText(R.string.pause_game);
            } else {
                gameController.pauseGame();
                button.setText(R.string.resume_game);
            }
        }
    }
}
