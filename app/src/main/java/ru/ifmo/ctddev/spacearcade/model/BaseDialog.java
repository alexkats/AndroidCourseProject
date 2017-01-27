package ru.ifmo.ctddev.spacearcade.model;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ru.ifmo.ctddev.spacearcade.MainActivity;
import ru.ifmo.ctddev.spacearcade.R;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public class BaseDialog implements View.OnTouchListener, Animation.AnimationListener {

    protected final MainActivity owner;
    private boolean isShowing;
    private ViewGroup rootLayout;
    private View rootView;

    public BaseDialog(MainActivity activity) {
        owner = activity;
    }

    protected void setContentView(int dialogResId) {
        ViewGroup activityRoot = (ViewGroup) owner.findViewById(android.R.id.content);
        rootView = LayoutInflater.from(owner).inflate(dialogResId, activityRoot, false);
        owner.applyTypeface(rootView);
    }

    public void show() {
        if (isShowing) {
            return;
        }

        isShowing = true;

        ViewGroup activityRoot = (ViewGroup) owner.findViewById(android.R.id.content);
        rootLayout = (ViewGroup) LayoutInflater.from(owner).inflate(R.layout.my_overlay_dialog, activityRoot, false);
        activityRoot.addView(rootLayout);
        rootLayout.setOnTouchListener(this);
        rootLayout.addView(rootView);
        startShowAnimation();
    }

    private void startShowAnimation() {
        Animation dialogIn = AnimationUtils.loadAnimation(owner, R.anim.enter_from_top);
        rootView.startAnimation(dialogIn);
    }

    public void dismiss() {
        if (!isShowing) {
            return;
        }

        isShowing = false;
        startHideAnimation();
    }

    private void startHideAnimation() {
        Animation dialogOut = AnimationUtils.loadAnimation(owner, R.anim.exit_through_top);
        dialogOut.setAnimationListener(this);
        rootView.startAnimation(dialogOut);
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void onAnimationStart(Animation paramAnimation) {
    }

    @Override
    public void onAnimationEnd(Animation paramAnimation) {
        hideViews();
        onDismissed();
    }

    protected void onDismissed() {
    }

    private void hideViews() {
        rootLayout.removeView(rootView);
        ViewManager activityRoot = (ViewManager) owner.findViewById(android.R.id.content);
        activityRoot.removeView(rootLayout);
    }

    @Override
    public void onAnimationRepeat(Animation paramAnimation) {
    }
}
