package com.bespectacled.modernbeta.biome.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinRegistryInvoker;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class BiomeSettingsRegistry {
    public static final RegistryKey<Registry<Codec<? extends BiomeSettings>>> BIOME_SETTINGS_KEY;
    public static final Registry<Codec<? extends BiomeSettings>> BIOME_SETTINGS;

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.<T>ofRegistry(ModernBeta.createId(registryId));
    }
    
    static {
        BIOME_SETTINGS_KEY = createRegistryKey("worldgen/biome_settings");
        BIOME_SETTINGS = MixinRegistryInvoker.invokeCreate(BIOME_SETTINGS_KEY, Lifecycle.stable(), () -> BiomeSettings.CODEC);
    }
}
