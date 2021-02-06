package com.bespectacled.modernbeta.biome.settings;

import java.util.function.Function;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;

import net.minecraft.util.registry.Registry;

public abstract class BiomeSettings {
    public static final Codec<BiomeSettings> CODEC;
    
    protected abstract Codec<? extends BiomeSettings> getCodec();
    
    static {
        Registry.register(BiomeSettingsRegistry.BIOME_SETTINGS, ModernBeta.createId("beta"), BetaBiomeSettings.CODEC);
        Registry.register(BiomeSettingsRegistry.BIOME_SETTINGS, ModernBeta.createId("vanilla"), VanillaBiomeSettings.CODEC);
        Registry.register(BiomeSettingsRegistry.BIOME_SETTINGS, ModernBeta.createId("single"), SingleBiomeSettings.CODEC);
        Registry.register(BiomeSettingsRegistry.BIOME_SETTINGS, ModernBeta.createId("plus"), PlusBiomeSettings.CODEC);
        Registry.register(BiomeSettingsRegistry.BIOME_SETTINGS, ModernBeta.createId("indev"), IndevBiomeSettings.CODEC);
        
        CODEC = BiomeSettingsRegistry.BIOME_SETTINGS.dispatchStable(BiomeSettings::getCodec, Function.identity());
    }
}
