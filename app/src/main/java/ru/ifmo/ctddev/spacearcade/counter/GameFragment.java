package ru.ifmo.ctddev.spacearcade.counter;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.input.CompositeInputController;
import ru.ifmo.ctddev.spacearcade.model.FPSCounter;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameView;
import ru.ifmo.ctddev.spacearcade.move.GameController;
import ru.ifmo.ctddev.spacearcade.move.ParallaxBackground;
import ru.ifmo.ctddev.spacearcade.move.Player;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class GameFragment extends BaseFragment implements InputManager.InputDeviceListener {

    private GameEngine gameEngine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_play_pause).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_play_pause) {
                    pauseGameAndShowPauseScreen();
                }
            }
        });

        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.view_game);
                gameEngine = new GameEngine(getActivity(), gameView, 4);
                gameEngine.setInputController(new CompositeInputController(getView(), getMainActivity()));
                new ParallaxBackground(gameEngine, 20, R.drawable.seamless_space).addToGameEngine(gameEngine, 0);
                new GameController(gameEngine).addToGameEngine(gameEngine, 2);
                new Player(gameEngine).addToGameEngine(gameEngine, 3);
                new FPSCounter(gameEngine).addToGameEngine(gameEngine, 2);
                gameEngine.startGame();
                InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
                inputManager.registerInputDeviceListener(GameFragment.this, null);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (gameEngine.isGameRunning()) {
            pauseGameAndShowPauseScreen();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameEngine.stopGame();
        InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
        inputManager.unregisterInputDeviceListener(this);
    }

    private void pauseGameAndShowPauseScreen() {
        if (gameEngine.isGamePaused()) {
            return;
        }

        gameEngine.pauseGame();

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_screen_title)
                .setMessage(R.string.pause_screen_message)
                .setPositiveButton(R.string.resume_game, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameEngine.resumeGame();
                    }
                })
                .setNegativeButton(R.string.stop_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gameEngine.stopGame();
                        ((MainActivity) getActivity()).goBack();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        gameEngine.resumeGame();
                    }
                })
                .create().show();
    }

    @Override
    public boolean onBackPressed() {
        boolean result = false;

        if (gameEngine.isGameRunning()) {
            pauseGameAndShowPauseScreen();
            result = true;
        }

        return result;
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {

    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        if (gameEngine.isGameRunning()) {
            pauseGameAndShowPauseScreen();
        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }
}
