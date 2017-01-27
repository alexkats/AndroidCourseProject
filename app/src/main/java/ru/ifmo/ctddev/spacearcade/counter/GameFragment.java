package ru.ifmo.ctddev.spacearcade.counter;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.GameOverDialog;
import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.PauseDialog;
import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.input.CompositeInputController;
import ru.ifmo.ctddev.spacearcade.model.FPSCounter;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.GameView;
import ru.ifmo.ctddev.spacearcade.move.GameController;
import ru.ifmo.ctddev.spacearcade.move.ParallaxBackground;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

@SuppressWarnings("RefusedBequest")
public class GameFragment extends BaseFragment implements InputManager.InputDeviceListener, PauseDialog.PauseDialogListener, GameOverDialog.GameOverDialogListener {

    private GameEngine gameEngine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
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
    }

    @Override
    protected void onLayoutCompleted() {
        prepareAndStartGame();
    }

    private void prepareAndStartGame() {
        if (getView() == null) {
            return;
        }

        GameView gameView = (GameView) getView().findViewById(R.id.view_game);
        gameEngine = new GameEngine(getActivity(), gameView, 4);
        gameEngine.setInputController(new CompositeInputController(getView(), getMainActivity()));
        gameEngine.setSoundManager(getMainActivity().getSoundManager());
        new ParallaxBackground(gameEngine, 20, R.drawable.seamless_space).addToGameEngine(gameEngine, 0);
        new GameController(gameEngine, this).addToGameEngine(gameEngine, 2);
        new FPSCounter(gameEngine).addToGameEngine(gameEngine, 2);
        new ScoreGameObject(getView(), R.id.score_value).addToGameEngine(gameEngine, 0);
        new LivesCounter(getView(), R.id.lives_value).addToGameEngine(gameEngine, 0);
        gameEngine.startGame();
        InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
        inputManager.registerInputDeviceListener(this, null);
        gameView.postInvalidate();
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

    private void pauseGameAndShowPauseScreen() {
        if (gameEngine.isGamePaused()) {
            return;
        }

        gameEngine.pauseGame();
        PauseDialog dialog = new PauseDialog(getMainActivity());
        dialog.setListener(this);
        showDialog(dialog);
    }

    @Override
    public void exitGame() {
        gameEngine.stopGame();
        ((MainActivity) getActivity()).goBack();
    }

    @Override
    public void resumeGame() {
        gameEngine.resumeGame();
    }

    @Override
    public void startNewGame() {
        gameEngine.stopGame();
        prepareAndStartGame();
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
