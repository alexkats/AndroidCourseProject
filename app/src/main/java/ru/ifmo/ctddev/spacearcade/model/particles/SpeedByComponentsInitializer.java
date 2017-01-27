package ru.ifmo.ctddev.spacearcade.model.particles;

import java.util.Random;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class SpeedByComponentsInitializer implements ParticleInitializer {

    private final double minSpeedX;
    private final double maxSpeedX;
    private final double minSpeedY;
    private final double maxSpeedY;

    public SpeedByComponentsInitializer(double speedMinX, double speedMaxX, double speedMinY, double speedMaxY) {
        minSpeedX = speedMinX;
        maxSpeedX = speedMaxX;
        minSpeedY = speedMinY;
        maxSpeedY = speedMaxY;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.speedX = (r.nextDouble() * (maxSpeedX - minSpeedX) + minSpeedX) / 1000.0d;
        p.speedY = (r.nextDouble() * (maxSpeedY - minSpeedY) + minSpeedY) / 1000.0d;
    }
}
