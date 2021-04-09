package com.bespectacled.modernbeta.noise;

public final class PerlinOctaveNoiseCombined extends Noise {
    private PerlinOctaveNoise noiseGenerator0;
    private PerlinOctaveNoise noiseGenerator1;
    
    public PerlinOctaveNoiseCombined(PerlinOctaveNoise noiseGenerator0, PerlinOctaveNoise noiseGenerator1) {
        this.noiseGenerator0 = noiseGenerator0;
        this.noiseGenerator1 = noiseGenerator1;
    }
    
    public final double sample(double x, double y) {
        return this.noiseGenerator0.sample(x + this.noiseGenerator1.sample(x, y), y);
    }
}
