package ru.ifmo.ctddev.spacearcade.move;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.input.InputController;
import ru.ifmo.ctddev.spacearcade.model.GameController;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Alexey Katsman
 * @since 26.01.17
 */

public class Player extends GameObject {

    private static final int BULLET_POOL_SIZE = 6;
    private static final long TIME_BETWEEN_SHOTS_IN_MILLIS = 250;

    private final View view;
    private final TextView textView;
    private final ImageView spaceShip;
    private final double pixelFactor;
    private final double speed;
    private final List<Bullet> bullets = new ArrayList<>();
    private int maxX;
    private int maxY;
    private double positionX;
    private double positionY;
    private long timeSinceLastShot;

    public Player(View view) {
        this.view = view;
        maxX = view.getWidth() - view.getPaddingRight() - view.getPaddingLeft();
        maxY = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();

        pixelFactor = view.getHeight() / 400.0d;
        speed = pixelFactor * 100.0d / 1000.0d;

        textView = (TextView) view.findViewById(R.id.text_view_score);

        spaceShip = new ImageView(view.getContext());
        Drawable spaceShipDrawable = null;
        spaceShipDrawable = view.getContext().getResources().getDrawable(R.drawable.space_ship);
        spaceShip.setLayoutParams(new ViewGroup.LayoutParams(
                (int) (spaceShipDrawable.getIntrinsicWidth() * pixelFactor),
                (int) (spaceShipDrawable.getIntrinsicHeight() * pixelFactor)
        ));
        spaceShip.setImageDrawable(spaceShipDrawable);
        ((ViewGroup) view).addView(spaceShip);

        maxX -= spaceShipDrawable.getIntrinsicWidth() * pixelFactor;
        maxY -= spaceShipDrawable.getIntrinsicHeight() * pixelFactor;

        initBullets();
    }

    private void initBullets() {
        for (int i = 0; i < BULLET_POOL_SIZE; i++) {
            bullets.add(new Bullet(view, pixelFactor));
        }
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    @Override
    public void startGame() {
        positionX = maxX / 2.0d;
        positionY = maxY / 2.0d;
    }

    @Override
    public void onDraw() {
        textView.setText("[" + (int) positionX +
                ',' + (int) positionY + ']');
        spaceShip.animate()
                .translationX((int) positionX)
                .translationY((int) positionY)
                .setDuration(1)
                .start();
    }

    @Override
    public void onUpdate(long elapsedTimeInMillis, GameController gameController) {
        updatePosition(elapsedTimeInMillis, gameController.inputController);
        canFire(elapsedTimeInMillis, gameController);
    }

    private void updatePosition(long elapsedTimeInMillis, InputController inputController) {
        positionX += speed * inputController.horizontalFactor * elapsedTimeInMillis;

        if (positionX < 0) {
            positionX = 0;
        }

        if (positionX > maxX) {
            positionX = maxX;
        }

        positionY += speed * inputController.verticalFactor * elapsedTimeInMillis;

        if (positionY < 0) {
            positionY = 0;
        }

        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void canFire(long elapsedTimeInMillis, GameController gameController) {
        if (gameController.inputController.isFiringNow && timeSinceLastShot > TIME_BETWEEN_SHOTS_IN_MILLIS) {
            Bullet bullet = getBullet();

            if (bullet == null) {
                return;
            }

            bullet.init(this, positionX + spaceShip.getWidth() / 2.0d, positionY);
            gameController.addGameObject(bullet);
            timeSinceLastShot = 0;
        } else {
            timeSinceLastShot += elapsedTimeInMillis;
        }
    }

    private Bullet getBullet() {
        return bullets.isEmpty() ? null : bullets.remove(0);
    }
}
