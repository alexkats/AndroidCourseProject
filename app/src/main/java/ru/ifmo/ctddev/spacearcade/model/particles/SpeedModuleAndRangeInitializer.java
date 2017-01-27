package ru.ifmo.ctddev.spacearcade.model.particles;

import java.util.Random;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class SpeedModuleAndRangeInitializer implements ParticleInitializer {

    private final double speedMin;
    private final double speedMax;
    private final int minAngle;
    private final int maxAngle;

    public SpeedModuleAndRangeInitializer(double speedMin, double speedMax, int minAngle, int maxAngle) {
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        double speed = r.nextDouble() * (speedMax - speedMin) + speedMin;
        int angle;

        if (maxAngle == minAngle) {
            angle = minAngle;
        } else {
            angle = r.nextInt(maxAngle - minAngle) + minAngle;
        }

        double angleInRads = angle * Math.PI / 180f;
        p.speedX = speed * Math.cos(angleInRads) / 1000.0d;
        p.speedY = speed * Math.sin(angleInRads) / 1000.0d;
    }
}
