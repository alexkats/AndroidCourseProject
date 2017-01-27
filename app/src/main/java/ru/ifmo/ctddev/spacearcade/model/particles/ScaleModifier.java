package ru.ifmo.ctddev.spacearcade.model.particles;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class ScaleModifier implements ParticleModifier {

    private final double initialValue;
    private final double finalValue;

    private final long startTime;
    private final long endTime;
    private final long duration;

    private final double valueIncrement;

    public ScaleModifier(double initialValue, double finalValue, long startMilis, long endMilis) {
        this.initialValue = initialValue;
        this.finalValue = finalValue;
        startTime = startMilis;
        endTime = endMilis;
        duration = endTime - startTime;
        valueIncrement = this.finalValue - this.initialValue;
    }

    @Override
    public void apply(Particle particle, long miliseconds) {
        if (miliseconds < startTime) {
            particle.scale = initialValue;
        } else if (miliseconds > endTime) {
            particle.scale = finalValue;
        } else {
            double percentageValue = (miliseconds - startTime) * 1.0d / duration;
            particle.scale = initialValue + valueIncrement * percentageValue;
        }
    }
}
