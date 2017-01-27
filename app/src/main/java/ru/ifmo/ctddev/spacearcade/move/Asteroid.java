package ru.ifmo.ctddev.spacearcade.move;

import ru.ifmo.ctddev.spacearcade.R;
import ru.ifmo.ctddev.spacearcade.model.BodyType;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.Sprite;
import ru.ifmo.ctddev.spacearcade.model.particles.ParticleSystem;
import ru.ifmo.ctddev.spacearcade.model.particles.ScaleModifier;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class Asteroid extends Sprite {

    public static final int EXPLOSION_PARTICLES = 15;

    private final GameController gameController;

    private final double speed;
    private final ParticleSystem trailParticleSystem;
    private final ParticleSystem explisionParticleSystem;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    public Asteroid(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.asteroid, BodyType.Circular);
        speed = 200d * pixelFactor / 1000d;
        this.gameController = gameController;

        trailParticleSystem = new ParticleSystem(gameEngine, 50, R.drawable.particle_dust, 600)
                .addModifier(new ScaleModifier(1, 2, 200, 600))
                .setFadeOut(200);

        explisionParticleSystem = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, R.drawable.particle_asteroid, 700)
                .setSpeedRange(15, 40)
                .setFadeOut(300)
                .setInitialRotationRange(0, 360)
                .setRotationSpeedRange(-180, 180);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        x += speedX * elapsedMillis;
        y += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;

        if (rotation > 360) {
            rotation = 0;
        } else if (rotation < 0) {
            rotation = 360;
        }

        trailParticleSystem.setPosition(x + width / 2.0d, y + height / 2.0d);

        if (y > gameEngine.height) {
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void addToGameEngine(GameEngine gameEngine, int layer) {
        super.addToGameEngine(gameEngine, layer);
        trailParticleSystem.addToGameEngine(gameEngine, layer - 1);
        trailParticleSystem.emit(15);
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        trailParticleSystem.stopEmiting();
        trailParticleSystem.removeFromGameEngine(gameEngine);
    }

    @Override
    public void onRemovedFromGameEngine() {
        gameController.returnToPool(this);
    }

    public void explode(GameEngine gameEngine) {
        explisionParticleSystem.oneShot(gameEngine, x + width / 2.0d, y + height / 2.0d, EXPLOSION_PARTICLES);
    }

    public void init(GameEngine gameEngine) {
        double angle = gameEngine.random.nextDouble() * Math.PI / 3.0d - Math.PI / 6.0d;
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);

        x = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4.0d;
        y = -height;
        rotationSpeed = angle * (180d / Math.PI) / 250.0d;
        rotation = gameEngine.random.nextInt(360);

        trailParticleSystem.clearInitializers()
                .setInitialRotationRange(0, 360)
                .setRotationSpeedRange(rotationSpeed * 800, rotationSpeed * 1000)
                .setSpeedByComponentsRange(-speedY * 100, speedY * 100, speedX * 100, speedX * 100);
    }
}
