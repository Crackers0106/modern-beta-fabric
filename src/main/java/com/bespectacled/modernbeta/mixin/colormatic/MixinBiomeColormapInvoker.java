package com.bespectacled.modernbeta.mixin.colormatic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import io.github.kvverti.colormatic.colormap.BiomeColormap;

@Mixin(BiomeColormap.class)
public interface MixinBiomeColormapInvoker {
    @Invoker("getColor")
    public int invokeGetColor(double temp, double rain);
}
