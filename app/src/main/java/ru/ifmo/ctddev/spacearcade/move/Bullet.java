package ru.ifmo.ctddev.spacearcade.move;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.model.BodyType;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.ScreenGameObject;
import ru.ifmo.ctddev.spacearcade.model.Sprite;

/**
 * @author Alexey Katsman
 * @since 26.01.17
 */

public class Bullet extends Sprite {

    private final double speed;

    private Player owner;

    public Bullet(GameEngine gameEngine) {
        super(gameEngine, R.drawable.bullet, BodyType.Rectangular);
        speed = gameEngine.pixelFactor * -300.0d / 1000.0d;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        y += speed * elapsedMillis;
        if (y < -height) {
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void onRemovedFromGameEngine() {
        owner.releaseBullet(this);
    }

    public void init(Player parent, double positionX, double positionY) {
        x = positionX - width / 2.0d;
        y = positionY - height / 2.0d;
        owner = parent;
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            removeFromGameEngine(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.explode(gameEngine);
            a.removeFromGameEngine(gameEngine);
        }
    }
}
