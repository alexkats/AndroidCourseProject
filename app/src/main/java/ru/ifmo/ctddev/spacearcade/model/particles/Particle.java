package ru.ifmo.ctddev.spacearcade.model.particles;

import ru.ifmo.ctddev.spacearcade.model.BodyType;
import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.Sprite;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class Particle extends Sprite {

    private final ParticleSystem particleSystem;
    public double speedX;
    public double speedY;
    public double rotationSpeed;
    private long timeToLive;
    private long totalMillis;

    private Iterable<ParticleModifier> modifiers;

    protected Particle(ParticleSystem particleSystem, GameEngine gameEngine, int drawableRes) {
        super(gameEngine, drawableRes, BodyType.NONE);
        this.particleSystem = particleSystem;
    }

    @Override
    public void startGame(GameEngine gameEngine) {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        totalMillis += elapsedMillis;
        if (totalMillis > timeToLive) {
            removeFromGameEngine(gameEngine);
        } else {
            x += speedX * elapsedMillis;
            y += speedY * elapsedMillis;
            rotation += rotationSpeed * elapsedMillis / 1000d;

            for (ParticleModifier modifier : modifiers) {
                modifier.apply(this, totalMillis);
            }
        }
    }

    @Override
    public void removeFromGameEngine(GameEngine gameEngine) {
        super.removeFromGameEngine(gameEngine);
        particleSystem.returnToPool(this);
    }

    public void activate(GameEngine gameEngine, long timeToLive, double x, double y, Iterable<ParticleModifier> modifiers, int layer) {
        this.timeToLive = timeToLive;
        this.x = x - width / 2.0d;
        this.y = y - height / 2.0d;
        addToGameEngine(gameEngine, layer);
        this.modifiers = modifiers;
        totalMillis = 0;
    }
}