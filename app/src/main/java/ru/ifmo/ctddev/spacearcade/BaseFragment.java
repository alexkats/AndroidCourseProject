package ru.ifmo.ctddev.spacearcade;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import ru.ifmo.ctddev.spacearcade.model.BaseDialog;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class BaseFragment extends Fragment {

    private BaseDialog currentDialog;

    public void showDialog(BaseDialog dialog) {
        showDialog(dialog, false);
    }

    public void showDialog(BaseDialog newDialog, boolean dismissOtherDialog) {
        if (currentDialog != null && currentDialog.isShowing()) {
            if (dismissOtherDialog) {
                currentDialog.dismiss();
            } else {
                return;
            }
        }

        currentDialog = newDialog;
        currentDialog.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().applyTypeface(view);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public synchronized void onGlobalLayout() {
                if (getView() == null) {
                    return;
                }

                ViewTreeObserver viewTreeObserver = getView().getViewTreeObserver();

                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    onLayoutCompleted();
                }
            }
        });
    }

    protected void onLayoutCompleted() {
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public boolean onBackPressed() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
            return true;
        }

        return false;
    }
}
