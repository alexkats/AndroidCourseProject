package ru.ifmo.ctddev.spacearcade.model.particles;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public interface ParticleModifier {

    void apply(Particle particle, long miliseconds);
}
