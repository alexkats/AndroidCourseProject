package ru.ifmo.ctddev.spacearcade.model.particles;

import java.util.Random;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class RotationSpeedInitializer implements ParticleInitializer {

    private final double minRotationSpeed;
    private final double maxRotationSpeed;

    public RotationSpeedInitializer(double minRotationSpeed, double maxRotationSpeed) {
        this.minRotationSpeed = minRotationSpeed;
        this.maxRotationSpeed = maxRotationSpeed;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.rotationSpeed = r.nextDouble() * (maxRotationSpeed - minRotationSpeed) + minRotationSpeed;
    }
}
