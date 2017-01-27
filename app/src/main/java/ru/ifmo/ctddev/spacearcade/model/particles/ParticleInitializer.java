package ru.ifmo.ctddev.spacearcade.model.particles;

import java.util.Random;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public interface ParticleInitializer {

    void initParticle(Particle p, Random r);
}
