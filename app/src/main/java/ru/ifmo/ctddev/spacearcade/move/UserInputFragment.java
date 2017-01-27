package ru.ifmo.ctddev.spacearcade.move;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import ru.ifmo.ctddev.spacearcade.BaseFragment;
import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.input.BasicInputController;
import ru.ifmo.ctddev.spacearcade.model.GameController;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class UserInputFragment extends BaseFragment {

    private GameController gameController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                gameController = new GameController(getActivity());
                gameController.setInputController(new BasicInputController(getView()));
                gameController.addGameObject(new Player(getView()));
                gameController.startGame();
            }
        });
    }
}
