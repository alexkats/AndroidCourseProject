package ru.ifmo.ctddev.spacearcade.counter;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
}
