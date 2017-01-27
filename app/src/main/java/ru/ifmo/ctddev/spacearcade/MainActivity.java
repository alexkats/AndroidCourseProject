package ru.ifmo.ctddev.spacearcade;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import ru.ifmo.ctddev.spacearcade.counter.GameFragment;
import ru.ifmo.ctddev.spacearcade.counter.MainMenuFragment;

/**
 * @author Andrey Chernyshov
 * @since 25.01.17
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";

    private GamepadControllerListener gamepadControllerListener;

    public void setGamepadControllerListener(GamepadControllerListener gamepadControllerListener) {
        this.gamepadControllerListener = gamepadControllerListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (gamepadControllerListener != null && gamepadControllerListener.handleKeyEvent(event)) {
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    public void startGame() {
        goToFragment(new GameFragment());
    }

    private void goToFragment(BaseFragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int oldFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            @SuppressLint("InlinedApi")
            int newFlags = oldFlags
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            oldFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(oldFlags);
            } else {
                decorView.setSystemUiVisibility(newFlags);
            }
        }
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (gamepadControllerListener != null && gamepadControllerListener.handleGenericMotionEvent(event)) {
            return true;
        }

        return super.dispatchGenericMotionEvent(event);
    }
}
