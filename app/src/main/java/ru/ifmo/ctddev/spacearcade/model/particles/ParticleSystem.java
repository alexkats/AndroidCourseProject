package ru.ifmo.ctddev.spacearcade.model.particles;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.ifmo.ctddev.spacearcade.model.GameEngine;
import ru.ifmo.ctddev.spacearcade.model.ScreenGameObject;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class ParticleSystem extends ScreenGameObject {

    private final Random random;
    private final List<ParticleModifier> modifiers;
    private final List<ParticleInitializer> initializers;
    private final long timeToLive;

    private final double pixelFactor;

    private final List<Particle> particlePool = new ArrayList<>();

    private long totalTimeInMillis;

    private int activatedParticles;
    private double particlesPerMilisecond;

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean isEmiting;

    public ParticleSystem(GameEngine gameEngine, int maxParticles, int drawableRedId, long timeToLive) {
        random = new Random();

        modifiers = new ArrayList<>();
        initializers = new ArrayList<>();
        this.timeToLive = timeToLive;

        pixelFactor = gameEngine.pixelFactor;

        for (int i = 0; i < maxParticles; i++) {
            particlePool.add(new Particle(this, gameEngine, drawableRedId));
        }
    }

    public ParticleSystem setSpeedRange(double speedMin, double speedMax) {
        initializers.add(new SpeedModuleAndRangeInitializer(speedMin * pixelFactor, speedMax * pixelFactor, 0, 360));
        return this;
    }

    public ParticleSystem setSpeedModuleAndAngleRange(double speedMin, double speedMax, int minAngle, int maxAngle) {
        initializers.add(new SpeedModuleAndRangeInitializer(speedMin * pixelFactor, speedMax * pixelFactor, minAngle, maxAngle));
        return this;
    }

    public ParticleSystem setSpeedByComponentsRange(double speedMinX, double speedMaxX, double speedMinY, double speedMaxY) {
        initializers.add(new SpeedByComponentsInitializer(speedMinX * pixelFactor, speedMaxX * pixelFactor,
                speedMinY * pixelFactor, speedMaxY * pixelFactor));
        return this;
    }

    public ParticleSystem setFadeOut(long milisecondsBeforeEnd) {
        modifiers.add(new AlphaModifier(255, 0, timeToLive - milisecondsBeforeEnd, timeToLive));
        return this;
    }

    public ParticleSystem addModifier(ParticleModifier modifier) {
        modifiers.add(modifier);
        return this;
    }

    public ParticleSystem setInitialRotationRange(int minAngle, int maxAngle) {
        initializers.add(new RotationInitiazer(minAngle, maxAngle));
        return this;
    }

    public ParticleSystem setRotationSpeedRange(double minRotationSpeed, double maxRotationSpeed) {
        initializers.add(new RotationSpeedInitializer(minRotationSpeed, maxRotationSpeed));
        return this;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        // Do nothing
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (!isEmiting) {
            return;
        }

        totalTimeInMillis += elapsedMillis;

        while (!particlePool.isEmpty() && activatedParticles < particlesPerMilisecond * totalTimeInMillis) {
            activateParticle(gameEngine);
        }
    }

    public void emit(int particlesPerSecond) {
        activatedParticles = 0;
        totalTimeInMillis = 0;
        particlesPerMilisecond = particlesPerSecond / 1000.0d;
        isEmiting = true;
    }

    public void oneShot(GameEngine gameEngine, double x, double y, int numParticles) {
        this.x = x;
        this.y = y;
        isEmiting = false;

        for (int i = 0; !particlePool.isEmpty() && i < numParticles; i++) {
            activateParticle(gameEngine);
        }
    }

    private void activateParticle(GameEngine gameEngine) {
        Particle p = particlePool.remove(0);

        for (int i = 0; i < initializers.size(); i++) {
            initializers.get(i).initParticle(p, random);
        }

        p.activate(gameEngine, timeToLive, x, y, modifiers, layer);
        activatedParticles++;
    }

    public void returnToPool(Particle particle) {
        particlePool.add(particle);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void stopEmiting() {
        isEmiting = false;
    }

    public ParticleSystem clearInitializers() {
        initializers.clear();
        return this;
    }
}
