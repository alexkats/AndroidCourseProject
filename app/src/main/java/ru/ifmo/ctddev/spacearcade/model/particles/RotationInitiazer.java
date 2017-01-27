package ru.ifmo.ctddev.spacearcade.model.particles;

import java.util.Random;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class RotationInitiazer implements ParticleInitializer {

    private final int minAngle;
    private final int maxAngle;

    public RotationInitiazer(int minAngle, int maxAngle) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.rotation = r.nextInt(maxAngle - minAngle) + minAngle;
    }
}
