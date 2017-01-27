package ru.ifmo.ctddev.spacearcade.model.particles;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class AlphaModifier implements ParticleModifier {

    private final int initialValue;
    private final int finalValue;

    private final long startTime;
    private final long endTime;
    private final float duration;
    private final float valueIncrement;

    public AlphaModifier(int initialValue, int finalValue, long startMilis, long endMilis) {
        this.initialValue = initialValue;
        this.finalValue = finalValue;
        startTime = startMilis;
        endTime = endMilis;
        duration = endTime - startTime;
        valueIncrement = this.finalValue - this.initialValue;
    }

    @Override
    public void apply(Particle particle, long millis) {
        if (millis < startTime) {
            particle.alpha = initialValue;
        } else if (millis > endTime) {
            particle.alpha = finalValue;
        } else {
            double percentageValue = (millis - startTime) * 1.0d / duration;
            particle.alpha = (int) (initialValue + valueIncrement * percentageValue);
        }
    }
}
