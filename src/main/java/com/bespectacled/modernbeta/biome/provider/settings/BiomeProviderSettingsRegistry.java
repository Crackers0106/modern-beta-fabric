package com.bespectacled.modernbeta.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinRegistryInvoker;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class BiomeProviderSettingsRegistry {
    public static final RegistryKey<Registry<Codec<? extends BiomeProviderSettings>>> BIOME_PROVIDER_SETTINGS_KEY;
    public static final Registry<Codec<? extends BiomeProviderSettings>> BIOME_PROVIDER_SETTINGS;

    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.<T>ofRegistry(ModernBeta.createId(registryId));
    }
    
    static {
        BIOME_PROVIDER_SETTINGS_KEY = createRegistryKey("worldgen/biome_provider_settings");
        BIOME_PROVIDER_SETTINGS = MixinRegistryInvoker.invokeCreate(BIOME_PROVIDER_SETTINGS_KEY, Lifecycle.stable(), () -> BiomeProviderSettings.CODEC);
    }
}
