package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.function.Function;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;

import net.minecraft.util.registry.Registry;

public abstract class BiomeProviderSettings {
    public static final Codec<BiomeProviderSettings> CODEC;
    
    protected abstract Codec<? extends BiomeProviderSettings> getCodec();
    
    static {
        Registry.register(BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS, ModernBeta.createId("beta"), BetaBiomeProviderSettings.CODEC);
        Registry.register(BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS, ModernBeta.createId("vanilla"), VanillaBiomeProviderSettings.CODEC);
        Registry.register(BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS, ModernBeta.createId("single"), SingleBiomeProviderSettings.CODEC);
        Registry.register(BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS, ModernBeta.createId("plus"), PlusBiomeProviderSettings.CODEC);
        Registry.register(BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS, ModernBeta.createId("indev"), IndevBiomeProviderSettings.CODEC);
        
        CODEC = BiomeProviderSettingsRegistry.BIOME_PROVIDER_SETTINGS.dispatchStable(BiomeProviderSettings::getCodec, Function.identity());
    }
}
