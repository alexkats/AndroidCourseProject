package ru.ifmo.ctddev.spacearcade.move;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.model.GameController;
import ru.ifmo.ctddev.spacearcade.model.GameObject;

/**
 * @author Alexey Katsman
 * @since 26.01.17
 */

public class Bullet extends GameObject {

    private final ImageView imageView;
    private final double width;
    private final double height;

    private final double speed;

    private double positionX;
    private double positionY;
    private Player owner;

    public Bullet(View view, double pixelFactor) {
        Context context = view.getContext();
        speed = pixelFactor * -300.0d / 1000.0d;
        imageView = new ImageView(context);
        Drawable bulletDrawable = context.getResources().getDrawable(R.drawable.bullet);

        width = bulletDrawable.getIntrinsicWidth() * pixelFactor;
        height = bulletDrawable.getIntrinsicHeight() * pixelFactor;

        imageView.setLayoutParams(new ViewGroup.LayoutParams((int) width, (int) height));
        imageView.setImageDrawable(bulletDrawable);
        imageView.setVisibility(View.GONE);

        ((ViewGroup) view).addView(imageView);
    }

    public void init(Player owner, double positionX, double positionY) {
        this.positionX = positionX - width / 2;
        this.positionY = positionY - height / 2;
        this.owner = owner;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onDraw() {
        imageView.animate()
                .translationX((int) positionX)
                .translationY((int) positionY)
                .setDuration(1)
                .start();
    }

    @Override
    public void onUpdate(long elapsedTimeInMillis, GameController gameController) {
        positionY += speed * elapsedTimeInMillis;

        if (height < -positionY) {
            gameController.removeGameObject(this);
            owner.releaseBullet(this);
        }
    }

    @Override
    public void onGameObjectAddedToGameUIThread() {
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGameObjectRemovedFromGameUIThread() {
        imageView.setVisibility(View.GONE);
    }
}
