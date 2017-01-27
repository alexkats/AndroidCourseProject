package ru.ifmo.ctddev.spacearcade.move;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.input.InputController;
import ru.ifmo.ctddev.spacearcade.model.BodyType;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.ScreenGameObject;
import ru.ifmo.ctddev.spacearcade.model.Sprite;
import ru.ifmo.ctddev.spacearcade.model.particles.ParticleSystem;

/**
 * @author Alexey Katsman
 * @since 26.01.17
 */

public class Player extends Sprite {

    private static final int BULLET_POOL_SIZE = 6;
    private static final long TIME_BETWEEN_SHOTS_IN_MILLIS = 250;
    private static final int EXPLOSION_PARTICLES = 20;

    private final ParticleSystem engineFireParticle;
    private final ParticleSystem explosionParticleSystem1;
    private final ParticleSystem explosionParticleSystem2;

    private final List<Bullet> bullets = new ArrayList<>();

    private final int maxX;
    private final int maxY;

    private final double speed;
    private long timeSinceLastFire;

    public Player(GameEngine gameEngine) {
        super(gameEngine, R.drawable.space_ship, BodyType.Circular);
        speed = pixelFactor * 100.0d / 1000.0d;
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        initBulletPool(gameEngine);

        engineFireParticle = new ParticleSystem(gameEngine, 50, R.drawable.particle_smoke, 600)
                .setRotationSpeedRange(-30, 30)
                .setSpeedModuleAndAngleRange(50, 80, 60, 120)
                .setInitialRotationRange(0, 360)
                .setFadeOut(400);

        explosionParticleSystem1 = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_ship_explosion_1, 600)
                .setSpeedRange(30, 150)
                .setInitialRotationRange(0, 360)
                .setFadeOut(200);

        explosionParticleSystem2 = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_ship_explosion_2, 600)
                .setSpeedRange(30, 150)
                .setInitialRotationRange(0, 360)
                .setFadeOut(200);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < BULLET_POOL_SIZE; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    void releaseBullet(Bullet b) {
        bullets.add(b);
    }

    @Override
    public void startGame() {
        x = maxX / 2.0d;
        y = maxY / 2.0d;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        updatePosition(elapsedMillis, gameEngine.inputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    @Override
    public void addToGameEngine(GameEngine gameEngine, int layer) {
        super.addToGameEngine(gameEngine, layer);
        engineFireParticle.addToGameEngine(gameEngine, this.layer - 1);
        engineFireParticle.emit(12);
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        engineFireParticle.removeFromGameEngine(gameEngine);
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.inputController.isFiringNow && timeSinceLastFire > TIME_BETWEEN_SHOTS_IN_MILLIS) {
            Bullet b = getBullet();

            if (b == null) {
                return;
            }

            b.init(this, x + width / 2.0d, y);
            b.addToGameEngine(gameEngine, 1);
            timeSinceLastFire = 0;
        } else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    private Bullet getBullet() {
        return bullets.isEmpty() ? null : bullets.remove(0);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        x += speed * inputController.horizontalFactor * elapsedMillis;

        if (x < 0) {
            x = 0;
        }

        if (x > maxX) {
            x = maxX;
        }

        y += speed * inputController.verticalFactor * elapsedMillis;

        if (y < 0) {
            y = 0;
        }

        if (y > maxY) {
            y = maxY;
        }

        engineFireParticle.setPosition(x + width / 2.0d, y + height);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            removeFromGameEngine(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeFromGameEngine(gameEngine);
            engineFireParticle.stopEmiting();
            explosionParticleSystem1.oneShot(gameEngine, x + width / 2.0d, y + width / 2.0d, EXPLOSION_PARTICLES);
            explosionParticleSystem2.oneShot(gameEngine, x + width / 2.0d, y + width / 2.0d, EXPLOSION_PARTICLES);
        }
    }
}
