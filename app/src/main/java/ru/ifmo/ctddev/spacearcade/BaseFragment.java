package ru.ifmo.ctddev.spacearcade;

import android.app.Fragment;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class BaseFragment extends Fragment {

    public boolean onBackPressed() {
        return false;
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
